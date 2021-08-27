package com.deco2800.game.components.tasks;


import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Chases a target entity if they've been alerted or can see the target */
public class AlertableChaseTask extends ChaseTask implements PriorityTask {

  private boolean alerted = false;
  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public AlertableChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
  }

  private void alerted() {
    alerted = true;
  }
  private void unAlerted() {
    alerted = false;
  }

  /**
   * Add listener to melee ghost to detect event where ghost king (AlertCaller) trigger "alert"
   * @return 10 if alerted (always see the target), default otherwise
   */
  @Override
  public int getPriority() {
    for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
      //get event triggered
      if (entity.getEntityType() != null && entity.getEntityType().equals("AlertCaller")) {
          //todo: dont re-add to same entity
          entity.getEvents().addListener("alert", this::alerted);
          entity.getEvents().addListener("unAlert", this::unAlerted);
        }
      }
    if (alerted) {
      return 10;
    }
    return super.getPriority();
  }
}
