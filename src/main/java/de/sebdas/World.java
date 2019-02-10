package de.sebdas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

class World implements Observable {
  private static final int WIDTH_TILES  = 15;
  private static final int HEIGHT_TILES = 10;

  private final Snake snake;
  private final List<InvalidationListener> listeners;

  World() {
    this.snake = new Snake(this);
    this.listeners = new ArrayList<>();
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

  void pulse() {
    snake.pulse();
    notifyListeners();
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
}