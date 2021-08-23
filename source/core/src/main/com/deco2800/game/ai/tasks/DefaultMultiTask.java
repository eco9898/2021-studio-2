package com.deco2800.game.ai.tasks;

/**
 * A default task implementation that stores the associated entity and updates status when
 * starting/stopping a task. Removes some boilerplate code from each task.
 */
public abstract class DefaultMultiTask extends DefaultTask implements Task {
  protected Task currentTask;

  protected void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }
}
