package de.sebdas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("World")
class WorldTest {

  @Test
  void test_getWidth() {
    fail("TODO");
  }

  @Test
  void test_getHeight() {
    fail("TODO");
  }

  @Test
  void test_getSnake() {
    fail("TODO");
  }

  @Test
  void test_getFood() {
    fail("TODO");
  }

  @Test
  void test_pulse() {
    fail("TODO");
  }

  @Test
  void test_onLeft() {
    fail("TODO");
  }

  @Test
  void test_onRight() {
    fail("TODO");
  }

  @Test
  void test_onUp() {
    fail("TODO");
  }

  @Test
  void test_onDown() {
    fail("TODO");
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsFor_world_move")
  @DisplayName("move() should work as expected")
  void test_move(final Coordinate coordinate, final Direction direction, final Coordinate expectedCoordinate) {
    final World world = new World();

    final Coordinate moved = world.move(coordinate, direction);

    assertThat(moved).isEqualTo(expectedCoordinate);
  }

  private static Stream<Arguments> provideArgumentsFor_world_move() {
    return Stream.of(
        Arguments.of(new Coordinate( 1, 1), Directions.left() , new Coordinate( 0 ,1)),
        Arguments.of(new Coordinate( 1, 1), Directions.right(), new Coordinate( 2, 1)),
        Arguments.of(new Coordinate( 1 ,1), Directions.up()   , new Coordinate( 1, 0)),
        Arguments.of(new Coordinate( 1, 1), Directions.down() , new Coordinate( 1, 2)),

        Arguments.of(new Coordinate( 0, 1), Directions.left() , new Coordinate(14, 1)),
        Arguments.of(new Coordinate(14, 1), Directions.right(), new Coordinate( 0, 1)),
        Arguments.of(new Coordinate( 1, 0), Directions.up()   , new Coordinate( 1, 9)),
        Arguments.of(new Coordinate( 1, 9), Directions.down() , new Coordinate( 1, 0))
    );
  }

  @Test
  void test_togglePause() {
    fail("TODO");
  }
}