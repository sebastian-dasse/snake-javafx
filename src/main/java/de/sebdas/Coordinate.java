package de.sebdas;

import java.util.Objects;

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

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Coordinate that = (Coordinate) o;
    return x == that.x &&
           y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Coordinate{" +
           "x=" + x +
           ", y=" + y +
           '}';
  }

  Coordinate translated(final Direction direction) {
    return new Coordinate(getX() + direction.getX(),
                          getY() + direction.getY());
  }

  Coordinate flipped(final int width, final int height) {
    return new Coordinate(flip(getX(), width),
                          flip(getY(), height));
  }

  private int flip(final int position, final int size) {
    return (size + position) % size;
  }
}
