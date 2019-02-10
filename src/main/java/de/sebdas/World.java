package de.sebdas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class World implements Observable {
  private static final int WIDTH_TILES = 15;
  private static final int HEIGHT_TILES = 10;

  private final Snake snake;
  private final List<InvalidationListener> listeners;
  private final Random random;
  private Set<Coordinate> food;
  private boolean paused;

  World() {
    this.snake = new Snake(createInitialHead(), this::move);
    this.listeners = new ArrayList<>();
    this.random = new Random(System.nanoTime());
    this.food = createFood();
    this.paused = false;
  }

  private Coordinate createInitialHead() {
    return new Coordinate(getWidth() / 2, getHeight() / 2);
  }

  @Override
  public void addListener(final InvalidationListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(final InvalidationListener listener) {
    listeners.remove(listener);
  }

  private void notifyListeners() {
    listeners.forEach(listener -> listener.invalidated(this));
  }

  int getWidth() {
    return WIDTH_TILES;
  }

  int getHeight() {
    return HEIGHT_TILES;
  }

  Snake getSnake() {
    return snake;
  }

  Set<Coordinate> getFood() {
    return food;
  }

  void pulse() {
    if (paused) return;
    updateSnake();
    notifyListeners();
  }

  private void updateSnake() {
    final boolean wasHeadInFood = food.remove(snake.getHead());
    if (wasHeadInFood) {
      snake.grow();
      if (food.isEmpty()) {
        food = createFood();
      }
    } else {
      snake.move();
    }
  }

  void onLeft() {
    snake.turnLeft();
  }

  void onRight() {
    snake.turnRight();
  }

  void onUp() {
    snake.turnUp();
  }

  void onDown() {
    snake.turnDown();
  }

  private Coordinate move(final Coordinate coordinate, final Direction direction) {
    return new Coordinate(flip(coordinate.getX() + direction.getX(), getWidth()),
                          flip(coordinate.getY() + direction.getY(), getHeight()));
  }

  private int flip(final int position, final int size) {
    return (size + position) % size;
  }

  private Set<Coordinate> createFood() {
    final int biteCount = random.nextInt(2) + 1;
    return Stream.generate(this::createRandomCoordinate)
                 .limit(biteCount)
                 .collect(toCollection(HashSet::new));
  }

  private Coordinate createRandomCoordinate() {
    return new Coordinate(random.nextInt(getWidth()), random.nextInt(getHeight()));
  }

  void togglePause() {
    paused = !paused;
  }
}
