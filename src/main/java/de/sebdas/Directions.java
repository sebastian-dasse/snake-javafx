package de.sebdas;

final class Directions {
  private Directions() {}

  private static final Direction LEFT = new DirectionLeft();
  private static final Direction RIGHT = new DirectionRight();
  private static final Direction UP = new DirectionUp();
  private static final Direction DOWN = new DirectionDown();

  static Direction left() {
    return LEFT;
  }

  static Direction right() {
    return RIGHT;
  }

  static Direction up() {
    return UP;
  }

  static Direction down() {
    return DOWN;
  }

  private static class DirectionLeft extends Direction {
    DirectionLeft() {
      super(-1,  0);
    }

    @Override
    Direction turnRight() {
      return this;
    }
  }

  private static class DirectionRight extends Direction {
    DirectionRight() {
      super( 1,  0);
    }

    @Override
    Direction turnLeft() {
      return this;
    }
  }

  private static class DirectionUp extends Direction {
    DirectionUp() {
      super( 0, -1);
    }

    @Override
    Direction turnDown() {
      return this;
    }
  }

  private static class DirectionDown extends Direction {
    DirectionDown() {
      super( 0,  1);
    }

    @Override
    Direction turnUp() {
      return this;
    }
  }
}
