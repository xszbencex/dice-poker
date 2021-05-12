package dicepoker.javafx.controller;

import dicepoker.gamestate.GameState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
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
        currentPlayerName.set(usernames.get(0));
        displayCurrentState();
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
        clearImages();
        this.gameState.getCurrentGameRound().nextTurn();
        currentPlayerName.setValue(this.gameState.getCurrentGameRound().getCurrentPlayerName());
        // this.displayCurrentState();
        if (this.gameState.getCurrentGameRound().isRoundOver()) {
            nextButton.setText("End round");
        }
    }

    private void displayCurrentState() {
        List<Integer> numbers = this.gameState.getCurrentGameRound().getThrownNumbers();
        for (int i = 0; i < 5; i++) {
            imageViews.get(i).setImage(diceImages.get(numbers.get(i) - 1));
        }
    }

    private void clearImages() {
        imageViews.forEach(imageView -> imageView.setImage(null));
    }
}
