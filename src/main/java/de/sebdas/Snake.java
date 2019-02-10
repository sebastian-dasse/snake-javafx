package de.sebdas;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.Arrays.asList;

class Snake {
  private static final double ANIMATION_TIME_SEC = 5.0;

  private final World world;
  private Direction direction;
  private final Deque<Coordinate> segments;

  Snake(final World world) {
    this.world = world;
    this.segments = new ArrayDeque<>(asList(
        new Coordinate(1, 0),
        new Coordinate(0, 0)
    ));
    this.direction = Directions.right();
  }

  Coordinate getHead() {
    return segments.getFirst();
  }

  Deque<Coordinate> getSegments() {
    return segments;
  }

  void move() {
    segments.addFirst(nextHeadPosition());
    segments.removeLast();
  }

  void grow() {
    segments.addFirst(nextHeadPosition());
  }

  private Coordinate nextHeadPosition() {
    return world.move(segments.getFirst(), direction);
  }

  void turnLeft() {
    direction = direction.turnLeft();
  }

  void turnRight() {
    direction = direction.turnRight();
  }

  void turnUp() {
    direction = direction.turnUp();
  }

  void turnDown() {
    direction = direction.turnDown();
  }
}
