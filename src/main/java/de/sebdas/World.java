package de.sebdas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.*;

import static java.util.Arrays.asList;

class World implements Observable {
  private static final int WIDTH_TILES  = 15;
  private static final int HEIGHT_TILES = 10;

  private final Snake snake;
  private final List<InvalidationListener> listeners;
  private final Random random;
  private Set<Coordinate> food;

  World() {
    this.snake = new Snake(this);
    this.listeners = new ArrayList<>();
    this.random = new Random(System.nanoTime());
    this.food = createFood();
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
    snake.pulse();
    feedSnake();
    notifyListeners();
  }

  private void feedSnake() {
    final boolean didSnakeEat = food.remove(snake.getHead());
    if (didSnakeEat) {
      System.out.println("yum!");
      if (food.isEmpty()) {
        food = createFood();
      }
    }
  }

  void onLeft() {
    snake.moveLeft();
  }

  void onRight() {
    snake.moveRight();
  }

  void onUp() {
    snake.moveUp();
  }

  void onDown() {
    snake.moveDown();
  }

  Coordinate move(final Coordinate coordinate, final Direction direction) {
    return new Coordinate(flip(coordinate.getX() + direction.getX(), getWidth()),
                          flip(coordinate.getY() + direction.getY(), getHeight()));
  }

  private int flip(final int position, final int size) {
    return (size + position) % size;
  }

  private Set<Coordinate> createFood() {

    // TODO:
    final int biteCount = random.nextInt(2) + 1;

    return new HashSet<>(asList(createRandomCoordinate(), createRandomCoordinate()));
  }

  private Coordinate createRandomCoordinate() {
    return new Coordinate(random.nextInt(getWidth()), random.nextInt(getHeight()));
  }
}
