package dicepoker.gamestate;

import lombok.Data;

/**
 * Class representing a player of the game
 * by it's username and number of round won.
 */
@Data
public class Player {
    /**
     * The username of the player.
     */
    String username;

    /**
     * The number of wins the player has won.
     */
    int roundsWon;

    Player(String username) {
        this.username = username;
        this.roundsWon = 0;
    }
}
