package dicepoker.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class representing the result of a player from previous games.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PlayerResult {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The username of the player.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The number of games where the play won.
     */
    @Column(nullable = false)
    private int first;

    /**
     * The number of games where the player placed second.
     */
    @Column(nullable = false)
    private int second;

    /**
     * The number of games where the player placed third.
     */
    @Column(nullable = false)
    private int third;

    /**
     * The number of games where the player placed fourth.
     */
    @Column(nullable = false)
    private int fourth;
}
