package de.sebdas;

import java.util.Objects;

final class Coordinate {
  private final int x;
  private final int y;

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
  public String toString() {
    return "Coordinate{" +
           "x=" + x +
           ", y=" + y +
           '}';
  }
}
