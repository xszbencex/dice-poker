package dicepoker.gamestate;

import lombok.ToString;

@ToString
public class Player {

    String username;
    int roundsWon;

    Player(String username) {
        this.username = username;
        this.roundsWon = 0;
    }
}
