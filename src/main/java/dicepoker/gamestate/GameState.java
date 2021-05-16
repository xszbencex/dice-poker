package dicepoker.gamestate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameState {

    private GameRound currentGameRound;
    private int roundCount;
    private boolean lastRound;
    private List<Player> playerList = new ArrayList<>();

    public GameState(List<String> usernames) {
        this.roundCount = 1;
        usernames.forEach(username -> this.playerList.add(new Player(username)));
        final int playerCount = this.playerList.size();
        if (playerCount < 4) {
            for (int i = 1; i <= 4 - playerCount; i++) {
                this.playerList.add(new Player("Bot" + i));
            }
        }
        this.currentGameRound = new GameRound(playerList);
        this.lastRound = false;
    }

    public void finishRound() {
        Player winner = playerList.stream()
                .filter(player -> player.username.equals(this.currentGameRound.getRoundWinner()))
                .findFirst().get();
        ++winner.roundsWon;
        this.currentGameRound = new GameRound(playerList);
        ++this.roundCount;
    }
}
