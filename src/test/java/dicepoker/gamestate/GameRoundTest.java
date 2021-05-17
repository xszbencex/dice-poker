package dicepoker.gamestate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameRoundTest {

    GameRound gameRound;
    @BeforeEach
    void init() {
        List<Player> playerList = List.of(new Player("test1"), new Player("test2"),
                new Player("test3"), new Player("test4"));
        gameRound = new GameRound(playerList);
    }

    @Test
    void testDetermineThrowValueGivenBust() {
        gameRound.setThrownNumbers(List.of(1, 2, 3, 4, 6));

        gameRound.determineThrowValue();

        assertEquals(Hand.BUST, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.BUST.baseValue + 16, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenPair() {
        gameRound.setThrownNumbers(List.of(1, 1, 2, 3, 4));

        gameRound.determineThrowValue();

        assertEquals(Hand.ONE_PAIR, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.ONE_PAIR.baseValue + 100 + 9, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenTwoPair() {
        gameRound.setThrownNumbers(List.of(1, 1, 2, 2, 3));

        gameRound.determineThrowValue();

        assertEquals(Hand.TWO_PAIR, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.TWO_PAIR.baseValue + 100 + 200 + 3, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenThreeOfAKind() {
        gameRound.setThrownNumbers(List.of(1, 1, 1, 2, 3));

        gameRound.determineThrowValue();

        assertEquals(Hand.THREE_OF_A_KIND, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.THREE_OF_A_KIND.baseValue + 100 + 5, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenSmallStraight() {
        gameRound.setThrownNumbers(List.of(1, 2, 3, 4, 5));

        gameRound.determineThrowValue();

        assertEquals(Hand.SMALL_STRAIGHT, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.SMALL_STRAIGHT.baseValue, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenBigStraight() {
        gameRound.setThrownNumbers(List.of(2, 3, 4, 5, 6));

        gameRound.determineThrowValue();

        assertEquals(Hand.BIG_STRAIGHT, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.BIG_STRAIGHT.baseValue, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenFullHouse() {
        gameRound.setThrownNumbers(List.of(1, 1, 1, 2, 2));

        gameRound.determineThrowValue();

        assertEquals(Hand.FULL_HOUSE, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.FULL_HOUSE.baseValue + 100 + 4, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenFourOfAKind() {
        gameRound.setThrownNumbers(List.of(1, 1, 1, 1, 2));

        gameRound.determineThrowValue();

        assertEquals(Hand.FOUR_OF_A_KIND, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.FOUR_OF_A_KIND.baseValue + 100 + 2, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testDetermineThrowValueGivenFiveOfAKind() {
        gameRound.setThrownNumbers(List.of(1, 1, 1, 1, 1));

        gameRound.determineThrowValue();

        assertEquals(Hand.FIVE_OF_A_KIND, gameRound.getCurrentPlayer().getHand());
        assertEquals(Hand.FIVE_OF_A_KIND.baseValue + 100, gameRound.getCurrentPlayer().thrownValue);
    }

    @Test
    void testConstructFrequency() {
        assertEquals(List.of(1, 1, 1, 1, 0, 1), gameRound.constructFrequency(List.of(1, 2, 3, 4, 6)));
        assertEquals(List.of(2, 1, 1, 1, 0, 0), gameRound.constructFrequency(List.of(1, 1, 2, 3, 4)));
        assertEquals(List.of(1, 3, 0, 0, 0, 1), gameRound.constructFrequency(List.of(1, 2, 2, 2, 6)));
        assertEquals(List.of(2, 0, 0, 2, 0, 1), gameRound.constructFrequency(List.of(1, 1, 4, 4, 6)));
        assertEquals(List.of(0, 3, 2, 0, 0, 0), gameRound.constructFrequency(List.of(2, 2, 2, 3, 3)));
        assertEquals(List.of(0, 2, 0, 1, 0, 2), gameRound.constructFrequency(List.of(6, 2, 2, 4, 6)));
    }

    @Test
    void testSumOfOthers() {
        assertEquals(1 + 5 + 6, gameRound.sumOfOthers(List.of(1, 2, 0, 0, 1, 1), 2));
        assertEquals(3, gameRound.sumOfOthers(List.of(0, 0, 1, 0, 2, 2), 2));
        assertEquals(2, gameRound.sumOfOthers(List.of(2, 0, 0, 3, 0, 0), 3));
        assertEquals(5, gameRound.sumOfOthers(List.of(0, 0, 4, 0, 1, 0), 4));
    }

    @Test
    void testGetCurrentPlayer() {
        gameRound.setCurrentPlayerIndex(2);

        assertEquals(gameRound.getPlayerList().get(2), gameRound.getCurrentPlayer());
    }

    @Test
    void testGetWinner() {
        gameRound.getPlayerList().get(0).setThrownValue(16);
        gameRound.getPlayerList().get(1).setThrownValue(1109);
        gameRound.getPlayerList().get(2).setThrownValue(2303);
        gameRound.getPlayerList().get(3).setThrownValue(3105);

        assertEquals(gameRound.getPlayerList().get(3).username, gameRound.getRoundWinner());
    }
}
