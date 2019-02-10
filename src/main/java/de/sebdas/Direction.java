package de.sebdas;

enum Direction {
  LEFT (-1,  0),
  RIGHT( 1,  0),
  UP   ( 0, -1),
  DOWN ( 0,  1);

  private final int x;
  private final int y;

  Direction(final int x, final int y) {
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
