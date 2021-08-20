package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

//This class has been imported to allow for a short delay for dash abilities
import java.util.Timer;
import java.util.TimerTask;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  /** Distance scale for moving in a diagonal direction. */
  public final float DIAGONAL_DISTANCE = 0.7071f;

  /** Multiplier difference for the dash ability. */
  public final float DASH_MULTIPLIER = 2.5f;

  /** When the player is pressing W, up is 1, else, up is 0. */
  private byte up = 0;

  /** When the player is pressing A, up is 1, else, up is 0. */
  private byte left = 0;

  /** When the player is pressing S, up is 1, else, up is 0. */
  private byte down = 0;

  /** When the player is pressing D, up is 1, else, up is 0. */
  private byte right = 0;

  /** While the player is in their dash, it is 1, else it is 0. */
  private byte dashing = 0;

  /** Used to change the speed of the player quickly. */
  private float speedMultiplier = 1;

  /** Stores the last system time since the dash ability was pressed.*/
  private long lastDash = 0L;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.W:
        this.up = 1;
        triggerWalkEvent();
        return true;
      case Keys.A:
        this.left = 1;
        triggerWalkEvent();
        return true;
      case Keys.S:
        this.down = 1;
        triggerWalkEvent();
        return true;
      case Keys.D:
        this.right = 1;
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        return true;
      case Keys.SHIFT_LEFT:
        this.speedMultiplier = 1.4f;
        triggerWalkEvent();
        return true;
      case Keys.CAPS_LOCK:
        dashing = 1;
        triggerDashEvent();
        update();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            speedMultiplier = 1f;
            dashing = 0;
            triggerWalkEvent();
          }
        }, 150);
      default:
        return false;
    }
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.W:
        this.up = 0;
        triggerWalkEvent();
        return true;
      case Keys.A:
        this.left = 0;
        triggerWalkEvent();
        return true;
      case Keys.S:
        this.down = 0;
        triggerWalkEvent();
        return true;
      case Keys.D:
        this.right = 0;
        triggerWalkEvent();
        return true;
      case Keys.SHIFT_LEFT:
        this.speedMultiplier = 1;
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers the walk event for the player character. The method will handle
   * diagonal movement to ensure that walk speed is consistent and check if
   * the player is no longer moving.
   */
  private void triggerWalkEvent() {
    if (dashing == 1) {
      return;
    }
    //Checks to see if the player should be static or is currently moving.
    if ((this.up - this.down) == 0 && (this.right - this.left) == 0) {
      entity.getEvents().trigger("walkStop");
    } else {
      calculateDistance(speedMultiplier);
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Triggers the dash event for the player character. The method will
   * call a movement at a certain speed.
   */
  private void triggerDashEvent() {
    calculateDistance(DASH_MULTIPLIER);
    entity.getEvents().trigger("dash", walkDirection);
  }

  /**
   * Helper function to set the X and Y coordinate movement distances.
   * The method will calculate the distance and check if the player is moving
   * in a diagonal movement and change the distances accordingly.
   *
   * @param multiplier multiplies the distance moved by the character
   */
  private void calculateDistance(float multiplier) {
    float x = this.right - this.left;
    float y = this.up - this.down;
    if (x != 0 && y != 0) {
      x = (float)(this.right - this.left) * DIAGONAL_DISTANCE;
      y = (float)(this.up - this.down) * DIAGONAL_DISTANCE;
    }
    walkDirection.x = x * multiplier;
    walkDirection.y = y * multiplier;
  }
}


// Original Code
///**
// * Input handler for the player for keyboard and touch (mouse) input.
// * This input handler only uses keyboard input.
// */
//public class KeyboardPlayerInputComponent extends InputComponent {
//  private final Vector2 walkDirection = Vector2.Zero.cpy();
//
//  public KeyboardPlayerInputComponent() {
//    super(5);
//  }
//
//  /**
//   * Triggers player events on specific keycodes.
//   *
//   * @return whether the input was processed
//   * @see InputProcessor#keyDown(int)
//   */
//  @Override
//  public boolean keyDown(int keycode) {
//    switch (keycode) {
//      case Keys.W:
//        walkDirection.add(Vector2Utils.UP);
//        triggerWalkEvent();
//        return true;
//      case Keys.A:
//        walkDirection.add(Vector2Utils.LEFT);
//        triggerWalkEvent();
//        return true;
//      case Keys.S:
//        walkDirection.add(Vector2Utils.DOWN);
//        triggerWalkEvent();
//        return true;
//      case Keys.D:
//        walkDirection.add(Vector2Utils.RIGHT);
//        triggerWalkEvent();
//        return true;
//      case Keys.SPACE:
//        entity.getEvents().trigger("attack");
//        return true;
//      default:
//        return false;
//    }
//  }
//
//  /**
//   * Triggers player events on specific keycodes.
//   *
//   * @return whether the input was processed
//   * @see InputProcessor#keyUp(int)
//   */
//  @Override
//  public boolean keyUp(int keycode) {
//    switch (keycode) {
//      case Keys.W:
//        walkDirection.sub(Vector2Utils.UP);
//        triggerWalkEvent();
//        return true;
//      case Keys.A:
//        walkDirection.sub(Vector2Utils.LEFT);
//        triggerWalkEvent();
//        return true;
//      case Keys.S:
//        walkDirection.sub(Vector2Utils.DOWN);
//        triggerWalkEvent();
//        return true;
//      case Keys.D:
//        walkDirection.sub(Vector2Utils.RIGHT);
//        triggerWalkEvent();
//        return true;
//      default:
//        return false;
//    }
//  }
//
//  private void triggerWalkEvent() {
//    if (walkDirection.epsilonEquals(Vector2.Zero)) {
//      entity.getEvents().trigger("walkStop");
//    } else {
//      entity.getEvents().trigger("walk", walkDirection);
//    }
//  }
//}
