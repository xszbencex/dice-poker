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
        roundCount = 1;
        usernames.forEach(username -> playerList.add(new Player(username)));
        final int playerCount = playerList.size();
        if (playerCount < 4) {
            for (int i = 1; i <= 4 - playerCount; i++) {
                playerList.add(new Player("Bot" + i));
            }
        }
        currentGameRound = new GameRound(playerList);
        lastRound = false;
    }

    public void finishRound() {
        currentGameRound = new GameRound(playerList);
        ++roundCount;
    }
}
