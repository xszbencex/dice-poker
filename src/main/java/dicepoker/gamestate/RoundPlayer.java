package dicepoker.gamestate;

import lombok.Data;

@Data
public class RoundPlayer {
    String username;
    Hand hand;
    int thrownValue;

    RoundPlayer(String username) {
        this.username = username;
        this.thrownValue = 0;
    }
}
