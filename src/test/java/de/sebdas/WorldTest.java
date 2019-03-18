package de.sebdas;

import javafx.beans.InvalidationListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static de.sebdas.SnakeAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@DisplayName("World")
@ExtendWith(MockitoExtension.class)
class WorldTest {

  private World world;

  @BeforeEach
  void setup() {
    world = new World();
  }

  @Test
  @DisplayName("addListener() should work as expected")
  void test_addListener(@Mock final InvalidationListener listenerMock) {
    world.addListener(listenerMock);

    world.pulse();

    verify(listenerMock).invalidated(any(World.class));
  }

  @Test
  @DisplayName("removeListener() should work as expected")
  void test_removeListener(@Mock final InvalidationListener listenerMock) {
    world.addListener(listenerMock);
    world.removeListener(listenerMock);

    world.pulse();

    verify(listenerMock, never()).invalidated(any(World.class));
  }

  @Test
  @DisplayName("getWidth() should return the expected width")
  void test_getWidth() {
    final int width = world.getWidth();

    assertThat(width).isEqualTo(15);
  }

  @Test
  @DisplayName("getHeight() should return the expected height")
  void test_getHeight() {
    final int height = world.getHeight();

    assertThat(height).isEqualTo(10);
  }

  @Test
  @DisplayName("getSnake() should return the expected snake")
  void test_getSnake() {
    final Snake snake = world.getSnake();

    assertThat(snake)
        .hasHead(new Coordinate(7, 5))
        .hasSegments(new Coordinate(7, 5),
                     new Coordinate(6, 5),
                     new Coordinate(5, 5))
        .hasDirection(Directions.right())
        .hasNoCollision();
  }

  @RepeatedTest(5)
  @DisplayName("getFood() should return legal coordinates")
  void test_getFood() {
    final Set<Coordinate> food = world.getFood();

    assertThat(food).isNotEmpty()
               .hasSizeLessThan(3)
               .allMatch(this::isInWorldBounds);
  }

  private boolean isInWorldBounds(final Coordinate coordinate) {
    final int x = coordinate.getX();
    final int y = coordinate.getY();
    return 0 <= x && x < world.getWidth() &&
           0 <= y && y < world.getHeight();
  }

  @Nested
  @DisplayName("pulse()")
  class Testing_pulse {

    @Test
    @DisplayName("should notify listeners and move the snake by one")
    void test_pulse(@Mock final InvalidationListener listenerMock) {
      world.addListener(listenerMock);

      world.pulse();

      assertThat(world.getSnake())
          .hasHead(new Coordinate(8, 5))
          .hasDirection(Directions.right())
          .hasNoCollision();
      verify(listenerMock).invalidated(any(World.class));
    }

    @Test
    @DisplayName("should notify listeners twice and move the snake by two")
    void test_pulse_twice(@Mock final InvalidationListener listenerMock) {
      world.addListener(listenerMock);

      world.pulse();
      world.pulse();

      assertThat(world.getSnake())
          .hasHead(new Coordinate(9, 5))
          .hasDirection(Directions.right())
          .hasNoCollision();
      verify(listenerMock, times(2)).invalidated(any(World.class));
    }

    @Test
    @DisplayName("should not notify listeners and not move the snake when paused")
    void test_pulse_when_paused(@Mock final InvalidationListener listenerMock) {
      world.addListener(listenerMock);
      world.togglePause();

      world.pulse();

      assertThat(world.getSnake())
          .hasHead(new Coordinate(7, 5))
          .hasDirection(Directions.right())
          .hasNoCollision();
      verify(listenerMock, never()).invalidated(any(World.class));
    }

    @Test
    void test_pulse_grows_when_head_in_food() {
      fail("TODO: we need to be able to inject food or the food creation mechanism");
    }

    @Test
    void test_pulse_does_not_grow_when_head_not_in_food() {
      fail("TODO");
    }
  }

  @Nested
  @DisplayName("change of direction")
  class Testing_turn {

    @Mock
    private Snake snakeMock;

    @BeforeEach
    void setup() {
      world.setSnake(snakeMock);
    }

    @Test
    @DisplayName("onLeft() should work as expected")
    void test_onLeft() {
      world.onLeft();

      verify(snakeMock).turnLeft();
    }

    @Test
    @DisplayName("onRight() should work as expected")
    void test_onRight() {
      world.onRight();

      verify(snakeMock).turnRight();
    }

    @Test
    @DisplayName("onUp() should work as expected")
    void test_onUp() {
      world.onUp();

      verify(snakeMock).turnUp();
    }

    @Test
    @DisplayName("onDown() should work as expected")
    void test_onDown() {
      world.onDown();

      verify(snakeMock).turnDown();
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsFor_world_move")
  @DisplayName("move() should work as expected")
  void test_move(final Coordinate coordinate, final Direction direction, final Coordinate expectedCoordinate) {
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

  @Nested
  @DisplayName("pause")
  class Testing_pause {

    @Test
    @DisplayName("should initially not be paused")
    void test_initially_not_pause() {
      assertThat(world.paused).isFalse();
    }

    @Test
    @DisplayName("togglePause() should pause the world when called once")
    void test_togglePause() {
      world.togglePause();

      assertThat(world.paused).isTrue();
    }

    @Test
    @DisplayName("togglePause() should pause the world when called three times")
    void test_togglePause_thrice() {
      world.togglePause();
      world.togglePause();
      world.togglePause();

      assertThat(world.paused).isTrue();
    }

    @Test
    @DisplayName("togglePause() should result in a running world when called twice")
    void test_togglePause_twice() {
      world.togglePause();
      world.togglePause();

      assertThat(world.paused).isFalse();
    }

    @Test
    @DisplayName("togglePause() should result in a running world when called four times")
    void test_togglePause_four_times() {
      world.togglePause();
      world.togglePause();
      world.togglePause();
      world.togglePause();

      assertThat(world.paused).isFalse();
    }
  }
}