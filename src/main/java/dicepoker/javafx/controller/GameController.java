package dicepoker.javafx.controller;

import dicepoker.gamestate.GameState;
import dicepoker.gamestate.Player;
import dicepoker.jpa.PlayerResult;
import dicepoker.jpa.PlayerResultDao;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2
public class GameController {

    private final static int NUMBER_OF_ROUNDS = 2;

    private final PlayerResultDao playerResultDao = PlayerResultDao.getInstance();
    private GameState gameState;
    private List<Image> diceImages;
    private final List<ImageView> imageViews = new ArrayList<>();
    private final IntegerProperty roundCount = new SimpleIntegerProperty(0);
    private final StringProperty currentPlayerName = new SimpleStringProperty();
    private final StringProperty currentHand = new SimpleStringProperty();

    @FXML
    private GridPane imageGrid;

    @FXML
    private Button nextButton;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Label roundCounterLabel;

    @FXML
    private Label numberOfRoundsLabel;

    @FXML
    private Label handLabel;

    @FXML
    private HBox roundHBox;

    public void initGameState(List<String> usernames) {
        log.info("First round");
        gameState = new GameState(usernames, NUMBER_OF_ROUNDS);
        this.gameResultInit();
        numberOfRoundsLabel.setText(String.valueOf(NUMBER_OF_ROUNDS));
        currentPlayerName.setValue(usernames.get(0));
        roundCount.setValue(gameState.getRoundCount());
        setImagesForCurrentState();
        currentHand.setValue(this.gameState.getCurrentGameRound().getCurrentHand());
    }

    @FXML
    public void initialize() {
        diceImages = IntStream.range(1, 7)
                .mapToObj(i -> new Image(getClass().getResource("/images/dice_" + i + ".png").toExternalForm()))
                .toList();
        currentPlayerLabel.textProperty().bind(currentPlayerName);
        roundCounterLabel.textProperty().bind(roundCount.asString());
        handLabel.textProperty().bind(currentHand);
        imageGrid.getChildren().forEach(node -> imageViews.add((ImageView) node));
    }

    @FXML
    public void nextAction(ActionEvent actionEvent) throws IOException {
        if (nextButton.getText().equals("End Game")) {
            this.endGame(actionEvent);
        } else if (gameState.getCurrentGameRound().isLastTurn()) {
            this.nextRound();
        } else {
            this.nextPlayer();
        }
    }

    private void nextPlayer() {
        log.info("Loading next player");
        clearImages();
        gameState.getCurrentGameRound().nextTurn();
        currentPlayerName.setValue(gameState.getCurrentGameRound().getCurrentPlayerName());
        currentHand.setValue(this.gameState.getCurrentGameRound().getCurrentHand());
        setImagesForCurrentState();
        if (this.gameState.getCurrentGameRound().isLastTurn()) {
            nextButton.setText("End Round");
        }
    }

    private void nextRound() {
        showWinnerAlert();
        gameState.finishRound();
        clearImages();
        if (gameState.isLastRound()) {
            this.displayNumberOfWins();
        } else {
            log.info("Loading next round");
            roundCount.setValue(gameState.getRoundCount());
            currentPlayerName.setValue(gameState.getCurrentGameRound().getCurrentPlayerName());
            currentHand.setValue(this.gameState.getCurrentGameRound().getCurrentHand());
            setImagesForCurrentState();
            nextButton.setText("Next Player");
        }
    }

    private void endGame(ActionEvent actionEvent) throws IOException {
        log.info("Game is over");
        log.debug("Saving result to database...");
        this.updateDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchController.class.getResource("/fxml/leaderboard.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setImagesForCurrentState() {
        List<Integer> numbers = this.gameState.getCurrentGameRound().getThrownNumbers();
        for (int i = 0; i < 5; i++) {
            imageViews.get(i).setImage(diceImages.get(numbers.get(i) - 1));
        }
    }

    private void clearImages() {
        imageViews.forEach(imageView -> imageView.setImage(null));
    }

    private void showWinnerAlert() {
        log.info("Showing winner alert");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText("The winner of the round is " + gameState.getCurrentGameRound().getRoundWinner());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/images/dice_6.png").toExternalForm()));
        alert.showAndWait();
    }

    private void displayNumberOfWins() {
        log.info("Showing game leaderboard");
        clearImages();
        numberOfRoundsLabel.setVisible(false);
        handLabel.setVisible(false);
        roundHBox.setVisible(false);
        currentPlayerName.setValue("Game Leaderboard");
        nextButton.setText("End Game");
        imageGrid.getColumnConstraints().get(4).setMinWidth(0);
        imageGrid.getColumnConstraints().get(4).setMaxWidth(0);
        imageGrid.getColumnConstraints().get(4).setPrefWidth(0);
        List<Player> playerLeaderBoard = gameState.getPlayerList().stream()
                .sorted(Comparator.comparingInt(Player::getRoundsWon).reversed())
                .toList();
        for (int i = 0; i < playerLeaderBoard.size(); i++) {
            Label placeLabel = new Label((i + 1) + ".");
            placeLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
            imageGrid.add(placeLabel, i, 0);
            GridPane.setMargin(placeLabel, new Insets(0, 0, 40, 0));
            Label usernameLabel = new Label(playerLeaderBoard.get(i).getUsername());
            usernameLabel.setFont(new Font(20));
            imageGrid.add(usernameLabel, i, 0);
            Label roundWonLabel = new Label("Rounds won: " + playerLeaderBoard.get(i).getRoundsWon());
            roundWonLabel.setFont(new Font(14));
            GridPane.setMargin(roundWonLabel, new Insets(40, 0, 0, 0));
            imageGrid.add(roundWonLabel, i, 0);
        }
    }

    private void updateDatabase() {
        List<Player> filteredAndSortedList = gameState.getPlayerList().stream()
                .sorted(Comparator.comparingInt(Player::getRoundsWon).reversed())
                .toList();
        for (int i = 0; i < filteredAndSortedList.size(); i++) {
            if (!filteredAndSortedList.get(i).getUsername().matches("^Bot[1-4]$")) {
                PlayerResult playerResult = playerResultDao.findByUsername(filteredAndSortedList.get(i).getUsername());
                log.info("Updating {}'s results", filteredAndSortedList.get(i).getUsername());
                switch (i) {
                    case 0: playerResult.setFirst(playerResult.getFirst() + 1);
                    case 1: playerResult.setSecond(playerResult.getSecond() + 1);
                    case 2: playerResult.setThird(playerResult.getThird() + 1);
                    case 3: playerResult.setFourth(playerResult.getFourth() + 1);
                }
                playerResultDao.update(playerResult);
            }
        }
    }

    private void gameResultInit() {
        log.debug("Creating new players in database");
        gameState.getPlayerList().stream()
                .filter(player -> !player.getUsername().startsWith("Bot"))
                .forEach(player -> {
                    PlayerResult playerResult = playerResultDao.findByUsername(player.getUsername());
                    if (playerResult == null) {
                        playerResultDao.persist(this.createPlayerResult(player.getUsername()));
                    }
                });
    }

    private PlayerResult createPlayerResult(String username) {
        return PlayerResult.builder()
                .username(username)
                .first(0)
                .second(0)
                .third(0)
                .fourth(0)
                .build();
    }
}
