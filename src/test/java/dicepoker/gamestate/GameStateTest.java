package dicepoker.gamestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    GameState gameState;
    @BeforeEach
    void init() {
        List<String> usernames = List.of("test1", "test2", "test3", "test4");
        gameState = new GameState(usernames, 10);
    }

    @Test
    void testInitializePlayersGivenOneName() {
        gameState.setPlayerList(new ArrayList<>());
        gameState.initializePlayers(List.of("test1"));

        assertEquals("test1", gameState.getPlayerList().get(0).username);
        assertEquals("Bot1", gameState.getPlayerList().get(1).username);
        assertEquals("Bot2", gameState.getPlayerList().get(2).username);
        assertEquals("Bot3", gameState.getPlayerList().get(3).username);
    }

    @Test
    void testInitializePlayersGivenTwoName() {
        gameState.setPlayerList(new ArrayList<>());
        gameState.initializePlayers(List.of("test1", "test2"));

        assertEquals("test1", gameState.getPlayerList().get(0).username);
        assertEquals("test2", gameState.getPlayerList().get(1).username);
        assertEquals("Bot1", gameState.getPlayerList().get(2).username);
        assertEquals("Bot2", gameState.getPlayerList().get(3).username);
    }

    @Test
    void testInitializePlayersGivenThreeName() {
        gameState.setPlayerList(new ArrayList<>());
        gameState.initializePlayers(List.of("test1", "test2", "test3"));

        assertEquals("test1", gameState.getPlayerList().get(0).username);
        assertEquals("test2", gameState.getPlayerList().get(1).username);
        assertEquals("test3", gameState.getPlayerList().get(2).username);
        assertEquals("Bot1", gameState.getPlayerList().get(3).username);
    }

    @Test
    void testInitializePlayersGivenFourName() {
        gameState.setPlayerList(new ArrayList<>());
        gameState.initializePlayers(List.of("test1", "test2", "test3", "test4"));

        assertEquals("test1", gameState.getPlayerList().get(0).username);
        assertEquals("test2", gameState.getPlayerList().get(1).username);
        assertEquals("test3", gameState.getPlayerList().get(2).username);
        assertEquals("test4", gameState.getPlayerList().get(3).username);
    }

    @Test
    void testFinishRound() {
        gameState.getCurrentGameRound().getPlayerList().get(0).setThrownValue(16);
        gameState.getCurrentGameRound().getPlayerList().get(1).setThrownValue(1109);
        gameState.getCurrentGameRound().getPlayerList().get(2).setThrownValue(2303);
        gameState.getCurrentGameRound().getPlayerList().get(3).setThrownValue(3105);

        gameState.finishRound();

        assertEquals(1, gameState.getPlayerList().get(3).roundsWon);
    }
}
