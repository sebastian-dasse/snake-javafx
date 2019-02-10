package de.sebdas;

abstract class Direction {
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

  Direction turnLeft() {
    return Directions.left();
  }

  Direction turnRight() {
    return Directions.right();
  }

  Direction turnUp() {
    return Directions.up();
  }

  Direction turnDown() {
    return Directions.down();
  }
}
