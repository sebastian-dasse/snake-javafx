package de.sebdas;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.Map;

public class SnakeGame extends Application {
  private static final double TILE_SIZE = 50.0;

  private final Canvas canvas;
  private final Painter painter;
  private GameLoop gameLoop;
  private KeyHandler keyHandler;

  public SnakeGame() {
    final World world = new World();
    this.canvas = new Canvas(scale(world.getWidth()), scale(world.getHeight()));
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    this.painter = new Painter(graphicsContext, TILE_SIZE);
    this.gameLoop = new GameLoop(world, painter);
    this.keyHandler = new KeyHandler(this, world);
  }

  /* not mandatory to have a main */
  public static void main(final String[] args) {
    launch();
  }

  static double scale(final int value) {
    return value * TILE_SIZE;
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

  @Override
  public void stop() {
    gameLoop.stop();
  }

  private Map<? extends KeyCombination, ? extends Runnable> defineAccelerators() {
    return Map.of(
        new KeyCodeCombination(KeyCode.LEFT), keyHandler::onLeft,
        new KeyCodeCombination(KeyCode.RIGHT), keyHandler::onRight,
        new KeyCodeCombination(KeyCode.UP), keyHandler::onUp,
        new KeyCodeCombination(KeyCode.DOWN), keyHandler::onDown,
        new KeyCodeCombination(KeyCode.P), keyHandler::togglePause,
        new KeyCodeCombination(KeyCode.S), keyHandler::restart,
        new KeyCodeCombination(KeyCode.X), keyHandler::cancel
    );
  }

  void reset() {
    gameLoop.stop();

    final World world = new World();
    this.gameLoop = new GameLoop(world, painter);
    this.keyHandler.setWorld(world);

    gameLoop.start();
  }
}
