package de.sebdas;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class Snake {
  private static final int INITIAL_LENGTH = 3;

  private final Deque<Coordinate> segments;
  private final BiFunction<Coordinate, Direction, Coordinate> move;
  Direction direction;
  private boolean collision;

  Snake(final Coordinate initialHead, final BiFunction<Coordinate, Direction, Coordinate> move) {
    this.move = move;
    this.segments = createInitialSegments(initialHead);
    this.direction = Directions.right();
    this.collision = false;
  }

  private Deque<Coordinate> createInitialSegments(final Coordinate initialHead) {
    return Stream.iterate(initialHead, prev -> move.apply(prev, Directions.left()))
                 .limit(INITIAL_LENGTH)
                 .collect(toCollection(ArrayDeque::new));
  }

  Coordinate getHead() {
    return segments.getFirst();
  }

  Collection<Coordinate> getSegments() {
    return Collections.unmodifiableCollection(segments);
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
    final Coordinate nextHeadPosition = move.apply(segments.getFirst(), direction);
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
