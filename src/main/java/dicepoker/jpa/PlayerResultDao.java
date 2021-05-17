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

    @Transactional
    public List<PlayerResult> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM PlayerResult r ORDER BY r.first DESC, r.second DESC, r.third DESC, r.fourth DESC", PlayerResult.class)
                .setMaxResults(n)
                .getResultList();
    }

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
