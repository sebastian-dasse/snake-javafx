package de.sebdas;

import java.util.ArrayList;
import java.util.List;

import static de.sebdas.Direction.*;
import static java.util.Arrays.asList;

class Snake {
  private static final double ANIMATION_TIME_SEC = 5.0;

  private final World world;
  private Coordinate headPosition;
  private Direction direction;
  private final List<Coordinate> segments;

  Snake(final World world) {
    this.world = world;
    this.headPosition = new Coordinate(0, 0);
    this.direction = RIGHT;
    this.segments = new ArrayList<>(asList(
        new Coordinate(1, 0),
        new Coordinate(0, 0)
    ));
  }

  int getHeadX() {
    return headPosition.getX();
  }

  int getHeadY() {
    return headPosition.getY();
  }

  void pulse() {
    headPosition = world.move(headPosition, direction);
  }

  void moveLeft() {
    if (direction == RIGHT) return;
//    headPosition = world.move(headPosition, LEFT);
    direction = LEFT;
  }

  void moveRight() {
    if (direction == LEFT) return;
//    headPosition = world.move(headPosition, RIGHT);
    direction = RIGHT;
  }

  void moveUp() {
    if (direction == DOWN) return;
//    headPosition = world.move(headPosition, UP);
    direction = UP;
  }

  void moveDown() {
    if (direction == UP) return;
//    headPosition = world.move(headPosition, DOWN);
    direction = DOWN;
  }
}
