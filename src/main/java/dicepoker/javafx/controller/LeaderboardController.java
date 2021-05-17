package dicepoker.javafx.controller;

import dicepoker.jpa.PlayerResult;
import dicepoker.jpa.PlayerResultDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;

@Log4j2
public class LeaderboardController {

    private final PlayerResultDao playerResultDao = PlayerResultDao.getInstance();

    @FXML
    private TableView<PlayerResult> leaderboardTable;

    @FXML
    private TableColumn<PlayerResult, String> username;

    @FXML
    private TableColumn<PlayerResult, Integer> first;

    @FXML
    private TableColumn<PlayerResult, Integer> second;

    @FXML
    private TableColumn<PlayerResult, Integer> third;

    @FXML
    private TableColumn<PlayerResult, Integer> fourth;

    @FXML
    private void initialize() {
        log.info("Displaying leaderboard");
        List<PlayerResult> leaderboardList = playerResultDao.findBest(10);

        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        first.setCellValueFactory(new PropertyValueFactory<>("first"));
        second.setCellValueFactory(new PropertyValueFactory<>("second"));
        third.setCellValueFactory(new PropertyValueFactory<>("third"));
        fourth.setCellValueFactory(new PropertyValueFactory<>("fourth"));

        ObservableList<PlayerResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(leaderboardList);

        leaderboardTable.setItems(observableResult);
    }

    @FXML
    public void restartGameAction(ActionEvent actionEvent) throws IOException {
        log.info("Restarting game");
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchController.class.getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
