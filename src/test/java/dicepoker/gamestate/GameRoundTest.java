package dicepoker.gamestate;

import org.junit.jupiter.api.Test;

import java.util.List;

public class GameRoundTest {

    @Test
    void testGetRoundWinner() {
        GameRound gameRound = new GameRound(List.of(new Player("test1"), new Player("test2")));
    }
}
