package dicepoker.jpa;

import lombok.extern.log4j.Log4j2;

import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

@Log4j2
public class PlayerResultDao extends GenericJpaDao<PlayerResult> {

    private static PlayerResultDao instance;

    private PlayerResultDao(Class<PlayerResult> entityClass) {
        super(entityClass);
    }

    public static PlayerResultDao getInstance() {
        if (instance == null) {
            instance = new PlayerResultDao(PlayerResult.class);
            instance.setEntityManager(Persistence.createEntityManagerFactory("dice-poker").createEntityManager());
        }
        return instance;
    }

    /**
     * Returns the list of {@code n} players, ordered by their number of wins then
     * second places, then third places and lastly fourth places.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results
     */
    @Transactional
    public List<PlayerResult> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM PlayerResult r ORDER BY r.first DESC, r.second DESC, r.third DESC, r.fourth DESC", PlayerResult.class)
                .setMaxResults(n)
                .getResultList();
    }

    /**
     * Returns a PlayerResult object with the given {@code username}, or
     * returns {@code null}, if there is no player with, the specific username.
     *
     * @param username the username of the player
     * @return the list of {@code n} best results
     */
    @Transactional
    public PlayerResult findByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT r FROM PlayerResult r WHERE r.username like '" + username + "'", PlayerResult.class)
                    .getSingleResult();
        } catch (Exception e) {
            log.debug("No player found with username {}", username);
            return null;
        }
    }
}
