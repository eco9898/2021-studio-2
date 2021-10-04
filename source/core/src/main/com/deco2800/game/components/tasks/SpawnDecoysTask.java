package com.deco2800.game.components.tasks;


import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Spawns in the boss's minions
 */
public class SpawnDecoysTask extends DefaultTask implements PriorityTask {

    /**
     * target entity (player)
     */
    private final Entity target;
    /**
     * game area
     */
    private final GameArea gameArea;
    /**
     * number of time enemy is spawn
     */
    private static int spawn = 0;

    /**
     * spawn the minion to help the boss attack the target
     *
     * @param target The entity to chase.
     */
    public SpawnDecoysTask(Entity target) {
        this.target = target;
        this.gameArea = ServiceLocator.getGameAreaService();
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canSpawn()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            spawn();
            spawn++;
        }
    }

    /**
     * Spawns in enemies according to the classes variables
     */
    public void spawn() {
        for (int i = 0; i < 4; i++) {
            Entity elf = NPCFactory.createLokiDecoy(target);
            ServiceLocator.getGameAreaService().incNum();
            Vector2 spawnPosition = owner.getEntity().getCenterPosition();
            switch (i % 4) {
                case 0:
                    spawnPosition.add(new Vector2(-1,1));
                    break;
                case 1:
                    spawnPosition.add(new Vector2(1, 1));
                    break;
                case 2:
                    spawnPosition.add(new Vector2(-1, -1));
                    break;
                case 3:
                    spawnPosition.add(new Vector2(1, -1));
                    break;
            }
            gameArea.spawnEntityAt(elf, spawnPosition, true, true);
        }
    }

    /**
     * return the priority of task
     *
     * @return 20 can spawn enemy, else -1
     */
    @Override
    public int getPriority() {
        if (canSpawn()) {
            return 20;
        }
        return -1;
    }

    /**
     * check if the boss is not inside the bound
     *
     * @return true if not, false otherwise
     */
    public boolean mapBound() {
        //todo: this isn't always true map can change sizes, wont always be 30x30
        return owner.getEntity().getPosition().x < 0
                && owner.getEntity().getPosition().y < 0
                && owner.getEntity().getPosition().x > 30
                && owner.getEntity().getPosition().y > 30;
    }

    /**
     * check if the minions can be spawned
     *
     * @return true if can spawn, false otherwise
     */
    private boolean canSpawn() {
        float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        float health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float ratio = health / maxHealth;

        if (ServiceLocator.getGameAreaService().getNumEnemy() == 0 && ratio < 0.5 && mapBound()) {
            return true;
        }

        if (ratio < 0.5 && spawn < 1) {
            return true;
        }
        return ratio < 0.25 && spawn < 2;
    }
}
