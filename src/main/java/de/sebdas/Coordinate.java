package de.sebdas;

final class Coordinate {
  private final int x;
  private final int y;

  Coordinate(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  int getX() {
    return x;
  }

  int getY() {
    return y;
  }
}
