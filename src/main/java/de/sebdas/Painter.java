package de.sebdas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static de.sebdas.SnakeGame.scale;

class Painter {

  private World world;
  private GraphicsContext gc;
  private final double tileSize;

  Painter(final World world, final GraphicsContext gc, final double tileSize) {
    this.world = world;
    this.gc = gc;
    this.tileSize = tileSize;
  }

  void paint() {
    clearCanvas();
    paintOnCanvas();
  }

  private void clearCanvas() {
    gc.setFill(Color.CORNSILK);
    gc.fillRect(0, 0, scale(world.getWidth()), scale(world.getHeight()));
  }

  private void paintOnCanvas() {
    final Snake snake = world.getSnake();
    gc.setFill(Color.CORNFLOWERBLUE);
    gc.fillRect(scale(snake.getHeadX()),
                scale(snake.getHeadY()),
                tileSize,
                tileSize);
  }

}
