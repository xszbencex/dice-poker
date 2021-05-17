package dicepoker.gamestate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameState {

    private GameRound currentGameRound;
    private int roundCount;
    private int numberOfRounds;
    private boolean lastRound;
    private List<Player> playerList = new ArrayList<>();

    public GameState(List<String> usernames, int numberOfRounds) {
        this.initializePlayers(usernames);
        this.numberOfRounds = numberOfRounds;
        this.roundCount = 1;
        this.currentGameRound = new GameRound(playerList);
        this.lastRound = false;
    }

    /**
     * Method that processes the end of a round, by
     * increasing the {@code roundsWon} property of
     * the winner {@link Player}, increases the round counter
     * and defines if this is the last round.
     */
    public void finishRound() {
        Player winner = playerList.stream()
                .filter(player -> player.username.equals(this.currentGameRound.getRoundWinner()))
                .findFirst().get();
        ++winner.roundsWon;
        if (this.roundCount != this.numberOfRounds) {
            this.currentGameRound = new GameRound(playerList);
            ++this.roundCount;
        } else {
            this.lastRound = true;
        }
    }

    /**
     * Adding players to the {@code playerList} with their username
     * and if there are less then 4 usernames given in the {@link dicepoker.javafx.controller.LaunchController}
     * then new {@link Player}s are added with BotN usernames.
     *
     * @param usernames usernames provided in the {@code TextFields}
     */
    private void initializePlayers(List<String> usernames) {
        usernames.forEach(username -> this.playerList.add(new Player(username)));
        final int playerCount = this.playerList.size();
        if (playerCount < 4) {
            for (int i = 1; i <= 4 - playerCount; i++) {
                this.playerList.add(new Player("Bot" + i));
            }
        }
    }
}
