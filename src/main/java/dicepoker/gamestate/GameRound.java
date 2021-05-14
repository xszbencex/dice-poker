package dicepoker.gamestate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class GameRound {

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private List<Player> playerList;

    private int currentPlayerIndex;
    private boolean lastTurn;
    private String roundWinner;
    private List<Integer> thrownNumbers;

    public GameRound(List<Player> players) {
        playerList = players;
        this.startRound();
    }

    public void nextTurn() {
        ++currentPlayerIndex;
        thrownNumbers = generateNumbers();
        determineThrowValue();
        if (currentPlayerIndex == 3) {
            lastTurn = true;
        }
    }

    public String getCurrentPlayerName() {
        return playerList.get(getCurrentPlayerIndex()).username;
    }

    private void startRound() {
        lastTurn = false;
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

    private void determineThrowValue() {
        Map<Integer, Integer> valueFrequency = new HashMap<>();
        thrownNumbers.forEach(number -> {
            if (valueFrequency.containsKey(number))
                valueFrequency.put(number, valueFrequency.get(number) + 1);
            else valueFrequency.put(number , 1);
        });
        valueFrequency.forEach((key, value) -> {});
    }
}
