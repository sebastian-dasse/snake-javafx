package de.sebdas;

import java.util.ArrayList;
import java.util.List;

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
    this.direction = Directions.right();

    this.segments = new ArrayList<>(asList(
        new Coordinate(1, 0),
        new Coordinate(0, 0)
    ));
  }

  Coordinate getHead() {
    return headPosition;
  }

  void pulse() {
    headPosition = world.move(headPosition, direction);
  }

  void moveLeft() {
    direction = direction.turnLeft();
  }

  void moveRight() {
    direction = direction.turnRight();
  }

  void moveUp() {
    direction = direction.turnUp();
  }

  void moveDown() {
    direction = direction.turnDown();
  }

}
