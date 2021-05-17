package dicepoker.gamestate;

import lombok.Data;

/**
 * Class representing a player for a round by it's username,
 * the {@link Hand} and the thrown value it has after throwing
 * in the current round.
 */
@Data
public class RoundPlayer {
    /**
     * The username of the player.
     */
    String username;

    /**
     * The {@link Hand} the player has after throwing.
     */
    Hand hand;

    /**
     * The value of the thrown numbers compared to other the others.
     */
    int thrownValue;

    RoundPlayer(String username) {
        this.username = username;
        this.thrownValue = 0;
    }
}
