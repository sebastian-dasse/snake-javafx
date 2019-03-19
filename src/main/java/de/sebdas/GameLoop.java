package de.sebdas;

import javafx.animation.AnimationTimer;

class GameLoop {
  private static final int UPDATE_INTERVAL_NANOS = 18_000_000 * 10;

  private final Painter painter;
  private final AnimationTimer animationTimer;
  private World world;

  GameLoop(final Painter painter) {
    this.painter = painter;
    this.animationTimer = createAnimationTimer();
  }

  void setWorld(final World world) {
    this.world = world;
    world.addListener(observable -> painter.paint(world));
  }

  private AnimationTimer createAnimationTimer() {
    return new AnimationTimer() {
      private long lastUpdate;

      @Override
      public void handle(final long now) {
        if (UPDATE_INTERVAL_NANOS <= now - lastUpdate) {
          lastUpdate = now;
          update();
        }
      }
    };
  }

  private void update() {
    if (world.noCollisionDetected()) {
      world.pulse();
    } else {
      painter.showWarning();
    }
  }

  void start() {
    painter.paint(world);
    animationTimer.start();
  }

  void stop() {
    animationTimer.stop();
  }
}
