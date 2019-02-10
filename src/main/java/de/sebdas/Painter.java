package de.sebdas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Set;

import static de.sebdas.SnakeGame.scale;

class Painter {
  private static final Color BACKGROUND_COLOR = Color.CORNSILK;
  private static final Color WARNING_COLOR = Color.RED;

  private final GraphicsContext gc;
  private final double tileSize;
  private World world;
  private Paint warningColor;

  Painter(final GraphicsContext gc, final double tileSize) {
    this.gc = gc;
    this.tileSize = tileSize;
    this.warningColor = WARNING_COLOR;
  }

  void paint(final World world) {
    this.world = world;
    clearCanvas();
    paintFood();
    paintSnake();
  }

  void showWarning() {
    gc.setStroke(warningColor);
    gc.setLineWidth(5.0);
    gc.strokeRect(0, 0, scale(world.getWidth()), scale(world.getHeight()));
    toggleWarningColor();
  }

  private void toggleWarningColor() {
    warningColor = warningColor.equals(WARNING_COLOR)
                   ? BACKGROUND_COLOR
                   : WARNING_COLOR;
  }

  private void clearCanvas() {
    gc.setFill(BACKGROUND_COLOR);
    gc.fillRect(0, 0, scale(world.getWidth()), scale(world.getHeight()));
  }

  private void paintFood() {
    final Set<Coordinate> food = world.getFood();
    gc.setFill(Color.DARKOLIVEGREEN);
    for (final Coordinate bite : food) {
      gc.fillRect(scale(bite.getX()),
                  scale(bite.getY()),
                  tileSize,
                  tileSize);
    }
  }

  private void paintSnake() {
    final Snake snake = world.getSnake();
    gc.setFill(Color.CORNFLOWERBLUE);
    for (final Coordinate segment : snake.getSegments()) {
      gc.fillRect(scale(segment.getX()),
                  scale(segment.getY()),
                  tileSize,
                  tileSize);
    }
  }
}
