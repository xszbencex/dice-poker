package dicepoker.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class DicePokerApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(DicePokerApplication.class.getResource("/fxml/launch.fxml")));
        stage.getIcons().add(new Image(DicePokerApplication.class.getResource("/images/dice_6.png").toExternalForm()));
        stage.setTitle("Dice Poker");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();

    }
}
