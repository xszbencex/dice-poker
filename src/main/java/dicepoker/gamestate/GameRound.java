package dicepoker.gamestate;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class representing a round in the game.
 */
@Data
@Log4j2
public class GameRound {

    private static final int SECOND_WEIGHT = 100;

    private List<RoundPlayer> playerList = new ArrayList<>();
    private int currentPlayerIndex;
    private boolean lastTurn;
    private List<Integer> thrownNumbers;

    public GameRound(List<Player> players) {
        players.forEach(player -> this.playerList.add(new RoundPlayer(player.username)));
        this.startRound();
    }

    /**
     * Method that processes the next round, by generating the
     * numbers, increasing the {@code currentPlayerIndex} and determining
     * if it's the last round.
     */
    public void nextTurn() {
        ++this.currentPlayerIndex;
        this.thrownNumbers = generateNumbers();
        this.determineThrowValue();
        if (this.currentPlayerIndex == 3) {
            this.lastTurn = true;
        }
    }

    /**
     * Returns the winner's username by choosing the player
     * with the most biggest thrown value, that is calculated in the
     * {@code determineThrowValue()} method.
     *
     * @see #determineThrowValue()
     * @return round winner's username
     */
    public String getRoundWinner() {
        return this.playerList.stream()
                .max(Comparator.comparing(RoundPlayer::getThrownValue))
                .map(RoundPlayer::getUsername).get();
    }

    /**
     * Returns the current {@link Player} whose numbers are generated in this round.
     *
     * @return current {@link Player}
     */
    public RoundPlayer getCurrentPlayer() {
        return this.playerList.get(currentPlayerIndex);
    }

    /**
     * Method that represents the start of the round by setting
     * default values and generates the first thrown numbers.
     */
    public void startRound() {
        this.lastTurn = false;
        this.currentPlayerIndex = 0;
        this.thrownNumbers = generateNumbers();
        this.determineThrowValue();
    }

    /**
     * Generates and returns a a {@code List} with 5 random numbers in the
     * range of 1-6, that represents the numbers that the current {@link Player} thrown
     * in his/her round.
     *
     * @return a a {@code List} with 5 random numbers in range of 1-6
     */
    public List<Integer> generateNumbers() {
        return IntStream.range(1, 6)
                .boxed()
                .map(i -> ThreadLocalRandom.current().nextInt(1, 6 + 1))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Calculates the thrown value by determining the max number of
     * same value has been thrown and determines the {@link Hand} of
     * the numbers and adds three weights.
     */
    public void determineThrowValue() {
        final RoundPlayer currentPlayer = this.getCurrentPlayer();
        List<Integer> frequencyByIndex = this.constructFrequency(this.thrownNumbers);
        int mostOfAKind = Collections.max(frequencyByIndex);
        int mostFrequent = frequencyByIndex.indexOf(mostOfAKind) + 1;
        switch (mostOfAKind) {
            case 5: {
                currentPlayer.hand = Hand.FIVE_OF_A_KIND;
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT;
                break;
            }
            case 4: {
                currentPlayer.hand = Hand.FOUR_OF_A_KIND;
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT
                        + this.sumOfOthers(frequencyByIndex, mostOfAKind);
                break;
            }
            case 3: {
                if (frequencyByIndex.contains(2)) {
                    currentPlayer.hand = Hand.FULL_HOUSE;
                } else {
                    currentPlayer.hand = Hand.THREE_OF_A_KIND;
                }
                currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT
                        + this.sumOfOthers(frequencyByIndex, mostOfAKind);
                break;
            }
            case 2: {
                if (frequencyByIndex.indexOf(2) != frequencyByIndex.lastIndexOf(2)) {
                    currentPlayer.hand = Hand.TWO_PAIR;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue +
                            (frequencyByIndex.indexOf(2) + frequencyByIndex.lastIndexOf(2) + 2) * SECOND_WEIGHT +
                            this.sumOfOthers(frequencyByIndex, mostOfAKind);
                } else {
                    currentPlayer.hand = Hand.ONE_PAIR;
                    currentPlayer.thrownValue = currentPlayer.hand.baseValue + mostFrequent * SECOND_WEIGHT
                            + this.sumOfOthers(frequencyByIndex, mostOfAKind);
                }
                break;
            }
            case 1: {
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
                break;
            }
            default: log.error("Error while determining thrown value!");
        }
    }

    /**
     * Returns a {@code List} where the indexes are representing
     * the thrown number shifted by -1, because of the {@code List} indexing,
     * and the values are representing the number of times the index has been thrown.
     *
     * @return a {@code List} with the frequency of the indexes
     */
    public List<Integer> constructFrequency(List<Integer> numList) {
        List<Integer> result = new ArrayList<>(Collections.nCopies(6, 0));
        numList.forEach(number -> result.set(number - 1, result.get(number - 1) + 1));
        return result;
    }

    /**
     * Returns the sum of the numbers in the {@code frequencyList} except
     * for the value of {@code mostOfAKind}.
     *
     * @param frequencyList the list of numbers to be summed
     * @param mostOfAKind the value that will be excluded
     * @return sum of the values meeting the condition
     */
    public int sumOfOthers(List<Integer> frequencyList, int mostOfAKind) {
        int sum = 0;
        for (int i = 0; i < frequencyList.size(); i++) {
            if (frequencyList.get(i) != mostOfAKind) {
                sum += (i + 1) * frequencyList.get(i);
            }
        }
        return sum;
    }
}
