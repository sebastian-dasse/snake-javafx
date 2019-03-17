package de.sebdas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Direction")
class DirectionTest {

  @Nested
  @TestInstance(Lifecycle.PER_CLASS)
  @DisplayName("getters")
  class Testing_getters {

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_getX")
    @DisplayName("getX() should work as expected for all directions")
    void test_getX(final Direction direction, final int expectedX) {
      assertThat(direction.getX()).isEqualTo(expectedX);
    }

    private Stream<Arguments> provideArgumentsFor_getX() {
      return Stream.of(
          Arguments.of(Directions.up()   ,  0),
          Arguments.of(Directions.right(),  1),
          Arguments.of(Directions.down() ,  0),
          Arguments.of(Directions.left() , -1)
      );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_getY")
    @DisplayName("getY() should work as expected for all directions")
    void test_getY(final Direction direction, final int expectedY) {
      assertThat(direction.getY()).isEqualTo(expectedY);
    }

    private Stream<Arguments> provideArgumentsFor_getY() {
      return Stream.of(
          Arguments.of(Directions.up()   , -1),
          Arguments.of(Directions.right(),  0),
          Arguments.of(Directions.down() ,  1),
          Arguments.of(Directions.left() ,  0)
      );
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsFor_toString")
  @DisplayName("toString() should work as expected for all directions")
  void test_toString(final Direction direction, final String expectedString) {
    assertThat(direction.toString()).isEqualTo(expectedString);
  }

  private static Stream<Arguments> provideArgumentsFor_toString() {
    return Stream.of(
        Arguments.of(Directions.up()   , "UP"),
        Arguments.of(Directions.right(), "RIGHT"),
        Arguments.of(Directions.down() , "DOWN"),
        Arguments.of(Directions.left() , "LEFT")
    );
  }

  @Nested
  @TestInstance(Lifecycle.PER_CLASS)
  @DisplayName("turn")
  class Testing_turn {

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_turnLeft")
    @DisplayName("turnLeft() should turn to the left for all directions but for RIGHT")
    void test_turnLeft(final Direction direction, final Direction expectedDirection) {
      final Direction resultingDirection = direction.turnLeft();
      assertThat(resultingDirection).isEqualTo(expectedDirection);
    }

    private Stream<Arguments> provideArgumentsFor_turnLeft() {
      return Stream.of(
          Arguments.of(Directions.up()   , Directions.left()),
          Arguments.of(Directions.right(), Directions.right()),
          Arguments.of(Directions.down() , Directions.left()),
          Arguments.of(Directions.left() , Directions.left())
      );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_turnRight")
    @DisplayName("turnRight() should turn to the right for all directions but for LEFT")
    void test_turnRight(final Direction direction, final Direction expectedDirection) {
      final Direction resultingDirection = direction.turnRight();
      assertThat(resultingDirection).isEqualTo(expectedDirection);
    }

    private Stream<Arguments> provideArgumentsFor_turnRight() {
      return Stream.of(
          Arguments.of(Directions.up()   , Directions.right()),
          Arguments.of(Directions.right(), Directions.right()),
          Arguments.of(Directions.down() , Directions.right()),
          Arguments.of(Directions.left() , Directions.left())
      );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_turnUp")
    @DisplayName("turnUp() should turn up for all directions but for DOWN")
    void test_turnUp(final Direction direction, final Direction expectedDirection) {
      final Direction resultingDirection = direction.turnUp();
      assertThat(resultingDirection).isEqualTo(expectedDirection);
    }

    private Stream<Arguments> provideArgumentsFor_turnUp() {
      return Stream.of(
          Arguments.of(Directions.up()   , Directions.up()),
          Arguments.of(Directions.right(), Directions.up()),
          Arguments.of(Directions.down() , Directions.down()),
          Arguments.of(Directions.left() , Directions.up())
      );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsFor_turnDown")
    @DisplayName("turnDown() should turn down for all directions but for UP")
    void test_turnDown(final Direction direction, final Direction expectedDirection) {
      final Direction resultingDirection = direction.turnDown();
      assertThat(resultingDirection).isEqualTo(expectedDirection);
    }

    private Stream<Arguments> provideArgumentsFor_turnDown() {
      return Stream.of(
          Arguments.of(Directions.up()   , Directions.up()),
          Arguments.of(Directions.right(), Directions.down()),
          Arguments.of(Directions.down() , Directions.down()),
          Arguments.of(Directions.left() , Directions.down())
      );
    }
  }
}
