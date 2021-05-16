package dicepoker.gamestate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Log4j2
public class GameRound {

    private static final int SECOND_WEIGHT = 100;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private List<RoundPlayer> playerList = new ArrayList<>();

    private int currentPlayerIndex;
    private boolean lastTurn;
    private List<Integer> thrownNumbers;

    public GameRound(List<Player> players) {
        players.forEach(player -> this.playerList.add(new RoundPlayer(player.username)));
        this.startRound();
    }

    public void nextTurn() {
        ++this.currentPlayerIndex;
        this.thrownNumbers = generateNumbers();
        this.determineThrowValue();
        if (this.currentPlayerIndex == 3) {
            this.lastTurn = true;
        }
    }

    public String getRoundWinner() {
        return this.playerList.stream()
                .max(Comparator.comparing(RoundPlayer::getThrownValue))
                .map(RoundPlayer::getUsername).get();
    }

    public String getCurrentPlayerName() {
        return this.playerList.get(currentPlayerIndex).username;
    }

    public String getCurrentHand() {
        return this.playerList.get(currentPlayerIndex).hand.name;
    }

    private void startRound() {
        this.lastTurn = false;
        this.currentPlayerIndex = 0;
        this.thrownNumbers = generateNumbers();
        this.determineThrowValue();
    }

    private List<Integer> generateNumbers() {
        return IntStream.range(1, 6)
                .boxed()
                .map(i -> ThreadLocalRandom.current().nextInt(1, 6 + 1))
                .sorted()
                .collect(Collectors.toList());
    }

    private void determineThrowValue() {
        final RoundPlayer currentPlayer = this.playerList.get(this.currentPlayerIndex);
        List<Integer> frequencyByIndex = this.constructFrequency();
        int mostOfAKind = Collections.max(frequencyByIndex);
        int mostFrequent = frequencyByIndex.indexOf(mostOfAKind);
        switch (mostOfAKind) {
            case 5 -> {
                currentPlayer.hand = Hand.FIVE_OF_A_KIND;
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT;
            }
            case 4 -> {
                currentPlayer.hand = Hand.FOUR_OF_A_KIND;
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT + this.sumOfOthers(frequencyByIndex, mostOfAKind);
            }
            case 3 -> {
                if (frequencyByIndex.contains(2)) {
                    currentPlayer.hand = Hand.FULL_HOUSE;
                } else {
                    currentPlayer.hand = Hand.THREE_OF_A_KIND;
                }
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT + this.sumOfOthers(frequencyByIndex, mostOfAKind);
            }
            case 2 -> {
                if (frequencyByIndex.indexOf(2) != frequencyByIndex.lastIndexOf(2)) {
                    currentPlayer.hand = Hand.TWO_PAIR;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue + (frequencyByIndex.indexOf(2) + frequencyByIndex.lastIndexOf(2)) * SECOND_WEIGHT +
                            this.sumOfOthers(frequencyByIndex, mostOfAKind);
                } else {
                    currentPlayer.hand = Hand.ONE_PAIR;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT + this.sumOfOthers(frequencyByIndex, mostOfAKind);
                }
            }
            case 1 -> {
                if (frequencyByIndex.get(6 - 1) == 1 && frequencyByIndex.get(0) == 0) {
                    currentPlayer.hand = Hand.BIG_STRAIGHT;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue;
                } else if (frequencyByIndex.get(0) == 1 && frequencyByIndex.get(6 - 1) == 0) {
                    currentPlayer.hand = Hand.SMALL_STRAIGHT;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue;
                } else {
                    currentPlayer.hand = Hand.BUST;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue;
                    currentPlayer.thrownValue += this.thrownNumbers.stream()
                            .mapToInt(Integer::valueOf)
                            .sum();
                }
            }
            default -> log.error("Error while determining thrown value!");
        }
    }

    private List<Integer> constructFrequency() {
        List<Integer> result = new ArrayList<>(Collections.nCopies(6, 0));
        this.thrownNumbers.forEach(number -> result.set(number - 1, result.get(number - 1) + 1));
        return result;
    }

    private int sumOfOthers(List<Integer> frequencyList, int mostOfAKind) {
        int sum = 0;
        for (int i = 0; i < frequencyList.size(); i++) {
            if (frequencyList.get(i) != mostOfAKind) {
                sum += (i + 1) * frequencyList.get(i);
            }
        }
        return sum;
    }
}
