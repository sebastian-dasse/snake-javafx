package de.sebdas;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Arrays;
import java.util.Objects;

class SnakeAssert extends AbstractAssert<SnakeAssert, Snake> {

  private SnakeAssert(final Snake snake) {
    super(snake, SnakeAssert.class);
  }

  static SnakeAssert assertThat(final Snake snake) {
    return new SnakeAssert(snake);
  }

  SnakeAssert hasSegments(final Coordinate... segments) {
    isNotNull();
    Assertions.assertThat(actual.getSegments())
              .withFailMessage("Expected snake's segments to be <%s> but was <%s>", Arrays.toString(segments), actual.getSegments())
              .containsExactly(segments);
    return this;
  }

  SnakeAssert hasHeadAt(final Coordinate head) {
    isNotNull();
    Assertions.assertThat(actual.getHead())
              .withFailMessage("Expected snake's head to be <%s> but was <%s>", head, actual.getHead())
              .isEqualTo(head);
    return this;
  }

  SnakeAssert hasDirection(final Direction direction) {
    isNotNull();
    if (!Objects.equals(actual.getDirection(), direction)) {
      failWithMessage("Expected snake's direction to be <%s> but was <%s>", direction, actual.getDirection());
    }
    return this;
  }

  SnakeAssert hasNoCollision() {
    isNotNull();
    Assertions.assertThat(actual.noCollisionDetected())
              .withFailMessage("Expected snake to have no collision but had collision detected")
              .isTrue();
    return this;
  }

  SnakeAssert hasCollision() {
    isNotNull();
    Assertions.assertThat(actual.noCollisionDetected())
              .withFailMessage("Expected snake to have a collision detected but had none")
              .isFalse();
    return this;
  }
}
