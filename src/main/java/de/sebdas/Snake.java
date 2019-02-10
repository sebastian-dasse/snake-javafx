package de.sebdas;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class Snake {
  private static final int INITIAL_LENGTH = 3;

  private final World world;
  private final Deque<Coordinate> segments;
  private Direction direction;
  private boolean collision;

  Snake(final World world) {
    this.world = world;
    this.segments = createInitialSegments();
    this.direction = Directions.right();
    this.collision = false;
  }

  private Deque<Coordinate> createInitialSegments() {
    final Coordinate initialHead = new Coordinate(world.getWidth() / 2, world.getHeight() / 2);
    return Stream.iterate(initialHead, prev -> new Coordinate(prev.getX() - 1, prev.getY()))
                 .limit(INITIAL_LENGTH)
                 .collect(toCollection(ArrayDeque::new));
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
