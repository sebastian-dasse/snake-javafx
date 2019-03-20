package de.sebdas;

import javafx.animation.AnimationTimer;

import java.util.function.Function;

class GameLoop {
  static final long UPDATE_INTERVAL_NANOS = 18_000_000 * 10;

  private final Painter painter;
  private final AnimationTimer animationTimer;
  private final World world;

  GameLoop(final World world, final Painter painter, final Function<GameLoop, AnimationTimer> animationTimerCreator) {
    this.painter = painter;
    this.animationTimer = animationTimerCreator.apply(this);
    this.world = world;

    this.world.addListener(observable -> this.painter.paint());
  }

  AnimationTimer createAnimationTimer() {
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

  void update() {
    if (world.noCollisionDetected()) {
      world.pulse();
    } else {
      painter.showWarning();
    }
  }

  void start() {
    painter.paint();
    animationTimer.start();
  }

  void stop() {
    animationTimer.stop();
  }
}
