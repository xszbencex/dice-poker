package dicepoker.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PlayerResult {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private int first;

    @Column(nullable = false)
    private int second;

    @Column(nullable = false)
    private int third;

    @Column(nullable = false)
    private int fourth;
}
