package dicepoker.gamestate;

import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class GameRound {

    private int currentPlayerIndex;
    private List<Player> playerList;
    private boolean isRoundOver;
    private List<Integer> thrownNumbers;

    public GameRound(List<Player> players) {
        playerList = players;
        this.startRound();
    }

    private void startRound() {
        isRoundOver = false;
        currentPlayerIndex = 0;
        thrownNumbers = generateNumbers();
    }

    private List<Integer> generateNumbers() {
        return IntStream.range(1, 6)
                .boxed()
                .map(i -> ThreadLocalRandom.current().nextInt(1, 6 + 1))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public void nextTurn() {
        thrownNumbers = getThrownNumbers();
        ++currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return playerList.get(getCurrentPlayerIndex()).username;
    }
}
