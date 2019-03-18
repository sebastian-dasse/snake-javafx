package de.sebdas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class World implements Observable {
  private static final int WIDTH_TILES = 15;
  private static final int HEIGHT_TILES = 10;

  private Snake snake;
  private final List<InvalidationListener> listeners;
  private final Random random;
  private final Set<Coordinate> food;
  boolean paused;

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

  /** intended for testing only */
  void setSnake(final Snake snake) {
    this.snake = snake;
  }

  void setFood(final Set<Coordinate> food) {
    this.food.clear();
    this.food.addAll(food);
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
    return Collections.unmodifiableSet(food);
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
        setFood(createFood());
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

  Coordinate move(final Coordinate coordinate, final Direction direction) {
    return coordinate.translated(direction)
                     .flipped(getWidth(), getHeight());
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
