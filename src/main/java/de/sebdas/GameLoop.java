package de.sebdas;

import java.util.Timer;
import java.util.TimerTask;

class GameLoop {
  private final World world;
  private Painter painter;
  private Timer timer;

  GameLoop(final World world, final Painter painter) {
    this.world = world;
    this.painter = painter;
    this.timer = new Timer();
  }

  void start() {
//    final long startNanoTime = System.nanoTime();

    painter.paint();
    world.addListener(observable -> painter.paint());
    initTimer();

    /*new AnimationTimer() {
      @Override
      public void handle(final long currentNanoTime) {
//        final long timSinceStartMillis = (startNanoTime - currentNanoTime) / 1_000_000;
        painter.paint();
      }
    }.start();*/
  }

  private void initTimer() {
    timer.scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
        world.pulse();
      }
    }, 0, 500);
  }

  void stop() {
    timer.cancel();
  }

}
