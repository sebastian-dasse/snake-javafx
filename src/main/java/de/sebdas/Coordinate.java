package de.sebdas;

class Coordinate {
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

  int getXIncremented(final Direction direction) {
    return x + direction.getX();
  }

  int getYIncremented(final Direction direction) {
    return y + direction.getY();
  }
}
