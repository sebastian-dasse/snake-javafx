package de.sebdas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

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
    paintFood();
    paintSnake();
  }

  private void clearCanvas() {
    gc.setFill(Color.CORNSILK);
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
    final Coordinate snakeHead = world.getSnake().getHead();
    gc.setFill(Color.CORNFLOWERBLUE);
    gc.fillRect(scale(snakeHead.getX()),
                scale(snakeHead.getY()),
                tileSize,
                tileSize);
  }

}
