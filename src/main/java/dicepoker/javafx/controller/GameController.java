package dicepoker.javafx.controller;

import dicepoker.gamestate.GameState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GameController {

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
    private Label handLabel;

    public void initGameState(List<String> usernames) {
        gameState = new GameState(usernames);
        currentPlayerName.setValue(usernames.get(0));
        roundCount.setValue(gameState.getRoundCount());
        setImagesForCurrentState();
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
    public void nextAction() {
        if (gameState.getCurrentGameRound().isLastTurn()) {
            showWinnerAlert();
            clearImages();
            gameState.finishRound();
            roundCount.setValue(gameState.getRoundCount());
            currentPlayerName.setValue(gameState.getCurrentGameRound().getCurrentPlayerName());
            setImagesForCurrentState();
            nextButton.setText("Next Player");
        } else {
            clearImages();
            gameState.getCurrentGameRound().nextTurn();
            currentPlayerName.setValue(gameState.getCurrentGameRound().getCurrentPlayerName());
            setImagesForCurrentState();
            if (this.gameState.getCurrentGameRound().isLastTurn()) {
                nextButton.setText("End Round");
            }
        }
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText("The winner is " + gameState.getCurrentGameRound().getRoundWinner());
        alert.showAndWait();
    }
}
