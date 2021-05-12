package dicepoker.gamestate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameState {

    private int roundCount;
    private List<Player> playerList = new ArrayList<>();
    private GameRound currentGameRound;

    public GameState(List<String> usernames) {
        roundCount = 0;
        usernames.forEach(username -> playerList.add(new Player(username)));
        final int playerCount = playerList.size();
        if (playerCount < 4) {
            for (int i = 1; i <= 4 - playerCount; i++) {
                playerList.add(new Player("Bot" + i));
            }
        }
        currentGameRound = new GameRound(playerList);
    }
}
