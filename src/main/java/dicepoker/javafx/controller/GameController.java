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

import java.util.List;
import java.util.stream.IntStream;

public class GameController {

    private GameState gameState;
    private List<String> usernames;
    private List<Image> diceImages;
    private final IntegerProperty roundCount = new SimpleIntegerProperty();
    private final StringProperty currentPlayer = new SimpleStringProperty();
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

    public void initUsername(List<String> usernames) {
        this.gameState.playerCount = usernames.size();
        this.usernames = usernames;
        currentPlayer.set(usernames.get(0));
    }

    @FXML
    public void initialize() {
        diceImages = IntStream.range(1, 7)
                .mapToObj(i -> new Image(getClass().getResource("/images/dice_" + i + ".png").toExternalForm()))
                .toList();
        ((ImageView) imageGrid.getChildren().get(0)).setImage(diceImages.get(0));
        ((ImageView) imageGrid.getChildren().get(1)).setImage(diceImages.get(1));
        ((ImageView) imageGrid.getChildren().get(2)).setImage(diceImages.get(2));
        ((ImageView) imageGrid.getChildren().get(3)).setImage(diceImages.get(3));
        ((ImageView) imageGrid.getChildren().get(4)).setImage(diceImages.get(4));
        ((ImageView) imageGrid.getChildren().get(5)).setImage(diceImages.get(5));
        gameState = new GameState();
        currentPlayerLabel.textProperty().bind(currentPlayer);
        roundCounterLabel.textProperty().bind(roundCount.asString());
        handLabel.textProperty().bind(currentHand);
    }

    @FXML
    public void nextAction() {

    }
}
