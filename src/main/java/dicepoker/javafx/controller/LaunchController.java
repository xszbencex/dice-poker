package dicepoker.javafx.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LaunchController {

    private int playerCount = 1;
    private final List<Label> labels = new ArrayList<>();
    private final List<TextField> textFields = new ArrayList<>();
    private final List<Button> removeButtons = new ArrayList<>();
    private final List<Button> addButtons = new ArrayList<>();

    @FXML
    private GridPane grid;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        final ObservableList<Node> elements = grid.getChildren();
        for (int i = 0; i < 14; i++) {
            switch (i) {
                case 0, 1, 2, 3 -> labels.add((Label) elements.get(i));
                case 4, 5, 6, 7 -> textFields.add((TextField) elements.get(i));
                case 8, 9, 10 -> removeButtons.add((Button) elements.get(i));
                case 11, 12, 13 -> addButtons.add((Button) elements.get(i));
            }
        }
    }

    @FXML
    public void addPlayer() {
        addButtons.get(playerCount - 1).setVisible(false);
        labels.get(playerCount).setVisible(true);
        textFields.get(playerCount).setVisible(true);
        removeButtons.get(playerCount - 1).setVisible(true);
        if (playerCount > 1) {
            removeButtons.get(playerCount - 2).setVisible(false);
        }
        if (playerCount < 3) {
            addButtons.get(playerCount).setVisible(true);
        }
        ++playerCount;
    }

    @FXML
    public void removePlayer() {
        --playerCount;
        addButtons.get(playerCount - 1).setVisible(true);
        labels.get(playerCount).setVisible(false);
        textFields.get(playerCount).setVisible(false);
        removeButtons.get(playerCount - 1).setVisible(false);
        if (playerCount > 1) {
            removeButtons.get(playerCount - 2).setVisible(true);
        }
        if (playerCount < 3) {
            addButtons.get(playerCount).setVisible(false);
        }
    }

    @FXML
    public void startGameAction(ActionEvent actionEvent) throws IOException {
        final List<String> usernames = this.createUsernameList();
        if (usernames.isEmpty()) {
            errorLabel.setVisible(true);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchController.class.getResource("/fxml/game.fxml"));
        Parent root = fxmlLoader.load();
        GameController controller = fxmlLoader.getController();
        controller.initGameState(this.createUsernameList());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2 - 150);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2 - 50);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private List<String> createUsernameList() {
        return textFields.stream()
                .map(TextField::getText)
                .filter(Predicate.not(String::isEmpty))
                .toList();
    }
}
