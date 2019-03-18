package de.sebdas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.sebdas.SnakeAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Snake")
class SnakeTest {

  private static final int WORLD_WIDTH  = 4;
  private static final int WORLD_HEIGHT = 2;

  private Snake snake;
  private static final Coordinate initialHead = new Coordinate(1, 1);

  @BeforeEach
  void setup() {
    snake = new Snake(initialHead,
                      (coordinate, direction) -> coordinate.translated(direction).flipped(WORLD_WIDTH, WORLD_HEIGHT));
  }

  @Nested
  @DisplayName("initially")
  class Testing_initial_state {

    @Test
    @DisplayName("should have head at expected position")
    void test_getHead() {
      assertThat(snake.getHead()).isEqualTo(initialHead);
    }

    @Test
    @DisplayName("should have segments at expected coordinates")
    void test_getSegments() {
      assertThat(snake.getSegments()).containsExactly(
          new Coordinate(1, 1),
          new Coordinate(0, 1),
          new Coordinate(3, 1)
      );
    }

    @Test
    @DisplayName("should have no collision")
    void test_noCollisionDetected() {
      assertThat(snake.noCollisionDetected()).isTrue();
    }

    @Test
    @DisplayName("should head left")
    void test_direction() {
      assertThat(snake).hasDirection(Directions.right());
    }
  }

  @Nested
  @DisplayName("move() in initially set direction ")
  class Testing_move {

    @Test
    @DisplayName("move() once should work as expected")
    void test_move() {
      snake.move();

      assertThat(snake)
          .hasHead(new Coordinate(2, 1))
          .hasSegments(new Coordinate(2, 1),
                       new Coordinate(1, 1),
                       new Coordinate(0, 1))
          .hasDirection(Directions.right())
          .hasNoCollision();
    }

    @Test
    @DisplayName("move() twice should work as expected")
    void test_move2() {
      snake.move();
      snake.move();

      assertThat(snake)
          .hasHead(new Coordinate(3, 1))
          .hasSegments(new Coordinate(3, 1),
                       new Coordinate(2, 1),
                       new Coordinate(1, 1))
          .hasDirection(Directions.right())
          .hasNoCollision();
    }

    @Test
    @DisplayName("move() three times should work as expected")
    void test_move3() {
      snake.move();
      snake.move();
      snake.move();

      assertThat(snake)
          .hasHead(new Coordinate(0, 1))
          .hasSegments(new Coordinate(0, 1),
                       new Coordinate(3, 1),
                       new Coordinate(2, 1))
          .hasDirection(Directions.right())
          .hasNoCollision();
    }
  }

  @Nested
  @DisplayName("grow() in initially set direction")
  class Testing_grow {

    @Test
    @DisplayName("grow() once should work as expected")
    void test_grow() {
      snake.grow();

      assertThat(snake)
          .hasHead(new Coordinate(2, 1))
          .hasSegments(new Coordinate(2, 1),
                       new Coordinate(1, 1),
                       new Coordinate(0, 1),
                       new Coordinate(3, 1))
          .hasDirection(Directions.right())
          .hasNoCollision();
    }

    @Test
    @DisplayName("grow() twice should work as expected")
    void test_grow2() {
      snake.grow();
      snake.grow();

      assertThat(snake)
          .hasHead(new Coordinate(3, 1))
          .hasSegments(new Coordinate(3, 1),
                       new Coordinate(2, 1),
                       new Coordinate(1, 1),
                       new Coordinate(0, 1),
                       new Coordinate(3, 1))
          .hasDirection(Directions.right())
          .hasCollision();
    }
  }

  @Nested
  @DisplayName("turn (with initial direction RIGHT)")
  class Testing_turn {

    @Test
    @DisplayName("turnLeft() should not turn but keep direction RIGHT")
    void test_turnLeft() {
      snake.turnLeft();

      assertThat(snake).hasDirection(Directions.right());
    }

    @Test
    @DisplayName("turnRight() should keep direction RIGHT")
    void test_turnRight() {
      snake.turnRight();

      assertThat(snake).hasDirection(Directions.right());
    }

    @Test
    @DisplayName("turnUp() should turn UP")
    void test_turnUp() {
      snake.turnUp();

      assertThat(snake).hasDirection(Directions.up());
    }

    @Test
    @DisplayName("turnDown() should turn DOWN")
    void test_turnDown() {
      snake.turnDown();

      assertThat(snake).hasDirection(Directions.down());
    }
  }
}
