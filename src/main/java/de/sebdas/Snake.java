package de.sebdas;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import static java.util.Arrays.asList;

class Snake {

  private final World world;
  private final Deque<Coordinate> segments;
  private Direction direction;
  private boolean collision;

  Snake(final World world) {
    this.world = world;
    this.segments = new ArrayDeque<>(asList(
        new Coordinate(1, 0),
        new Coordinate(0, 0)
    ));
    this.direction = Directions.right();
    this.collision = false;
  }

  Coordinate getHead() {
    return segments.getFirst();
  }

  Collection<Coordinate> getSegments() {
    return segments;
  }

  boolean noCollisionDetected() {
    return !collision;
  }

  void move() {
    segments.addFirst(nextHeadPosition());
    segments.removeLast();
  }

  void grow() {
    segments.addFirst(nextHeadPosition());
  }

  private Coordinate nextHeadPosition() {
    final Coordinate nextHeadPosition = world.move(segments.getFirst(), direction);
    collision = segments.contains(nextHeadPosition);
    return nextHeadPosition;
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
