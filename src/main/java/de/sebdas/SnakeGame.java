package de.sebdas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.Map;

public class SnakeGame extends Application {
  private static final double TILE_SIZE = 50.0;

  /* the main allows it to start the application with Gradle, otherwise it's not mandatory for a JavaFX Application */
  public static void main(final String[] args) {
    launch();
  }

  static double scale(final int value) {
    return value * TILE_SIZE;
  }

  private final Canvas canvas;
  private final World world;
  private GameLoop gameLoop;
  private KeyHandler keyHandler;

  public SnakeGame() {
    this.canvas = new Canvas();
    this.world = new World();

    final Painter painter = new Painter(canvas.getGraphicsContext2D(), TILE_SIZE, world);

    this.gameLoop = new GameLoop(world, painter, GameLoop::createAnimationTimer);
    this.keyHandler = new KeyHandler(this, world);

    canvas.setWidth(scale(world.getWidth()));
    canvas.setHeight(scale(world.getHeight()));
  }

  @Override
  public void start(final Stage stage) {
    final Scene scene = new Scene(new Group(canvas));
    scene.getAccelerators().putAll(defineAccelerators());

    stage.setTitle("Snake");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();

    gameLoop.start();
  }

  private Map<? extends KeyCombination, ? extends Runnable> defineAccelerators() {
    return Map.of(
        new KeyCodeCombination(KeyCode.LEFT), keyHandler::onLeft,
        new KeyCodeCombination(KeyCode.RIGHT), keyHandler::onRight,
        new KeyCodeCombination(KeyCode.UP), keyHandler::onUp,
        new KeyCodeCombination(KeyCode.DOWN), keyHandler::onDown,
        new KeyCodeCombination(KeyCode.P), keyHandler::togglePause,
        new KeyCodeCombination(KeyCode.S), keyHandler::restart,
        new KeyCodeCombination(KeyCode.X), keyHandler::cancel,
        new KeyCodeCombination(KeyCode.Y), keyHandler::yes,
        new KeyCodeCombination(KeyCode.N), keyHandler::no
    );
  }

  @Override
  public void stop() {
    gameLoop.stop();
  }

  void reset() {
    gameLoop.stop();
    world.reset();
    gameLoop.start();
  }

  void exit() {
    Platform.exit();
  }
}
