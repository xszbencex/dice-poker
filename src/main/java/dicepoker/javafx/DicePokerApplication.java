package dicepoker.javafx;

import dicepoker.jpa.PlayerResultDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class DicePokerApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        log.info("Starting application...");
        PlayerResultDao.getInstance();
        Parent root = FXMLLoader.load(Objects.requireNonNull(DicePokerApplication.class.getResource("/fxml/launch.fxml")));
        stage.getIcons().add(new Image(DicePokerApplication.class.getResource("/images/dice_6.png").toExternalForm()));
        stage.setTitle("Dice Poker");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
