package de.sebdas;

import javafx.beans.InvalidationListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static de.sebdas.SnakeAssert.assertThat;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("World")
@ExtendWith(MockitoExtension.class)
class WorldTest {

  private static final int MAX_FOOD = 2;

  private World world;

  @BeforeEach
  void setup() {
    world = new World();
  }

  @Nested
  @DisplayName("listeners")
  class Testing_listeners {

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
  }

  @Nested
  @DisplayName("getters and setters")
  class Testing_getters_and_setters {

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
                      .hasSizeLessThanOrEqualTo(MAX_FOOD)
                      .allMatch(this::isInWorldBounds);
    }

    private boolean isInWorldBounds(final Coordinate coordinate) {
      final int x = coordinate.getX();
      final int y = coordinate.getY();
      return 0 <= x && x < world.getWidth() &&
             0 <= y && y < world.getHeight();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "(1, 1)",
        "(1, 1), (2, 2)",
        "(2, 1), (4, 3), (8, 5)"
    })
    @DisplayName("setFood() should work as expected")
    void test_setFood(@ConvertWith(StringToCoordinateSetArgumentConverter.class) Set<Coordinate> food) {
      world.setFood(food);

      assertThat(world.getFood()).isEqualTo(food);
    }
  }

  @Nested
  @DisplayName("pulse()")
  class Testing_pulse {

    @Nested
    @DisplayName("the listeners")
    class Testing_listeners {

      @Test
      @DisplayName("should be notified when called once")
      void test_pulse_notifies(@Mock final InvalidationListener listenerMock) {
        world.addListener(listenerMock);

        world.pulse();

        verify(listenerMock).invalidated(any(World.class));
      }

      @Test
      @DisplayName("should be notified twice when called twice")
      void test_pulse_twice_notifies_twice(@Mock final InvalidationListener listenerMock) {
        world.addListener(listenerMock);

        world.pulse();
        world.pulse();

        verify(listenerMock, times(2)).invalidated(any(World.class));
      }

      @Test
      @DisplayName("should not be notified when paused")
      void test_pulse_does_not_notify_when_paused(@Mock final InvalidationListener listenerMock) {
        world.addListener(listenerMock);
        world.togglePause();

        world.pulse();

        verify(listenerMock, never()).invalidated(any(World.class));
      }
    }

    @Nested
    @DisplayName("the snake")
    class Testing_snake {

      @Test
      @DisplayName("should not move nor grow when paused")
      void test_pulse_does_not_move_nor_grow_when_paused(@Mock final Snake snakeMock) {
        world.setSnake(snakeMock);
        world.togglePause();

        world.pulse();

        verify(snakeMock, never()).move();
        verify(snakeMock, never()).grow();
      }

      @Test
      @DisplayName("should not touch food when paused")
      void test_pulse_does_not_eat_when_paused(@Mock final Snake snakeMock) {
        world.setSnake(snakeMock);
        final Set<Coordinate> food = Set.of(new Coordinate(1, 2), new Coordinate(3, 4));
        world.setFood(food);
        world.togglePause();

        world.pulse();

        assertThat(world.getFood()).isEqualTo(food);
      }

      @Test
      @DisplayName("should grow when head was in food")
      void test_pulse_grows_when_head_in_food(@Mock final Snake snakeMock) {
        final Coordinate headPosition = new Coordinate(1, 2);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setSnake(snakeMock);
        world.setFood(Set.of(headPosition, new Coordinate(3, 4)));

        world.pulse();

        verify(snakeMock).grow();
        verify(snakeMock, never()).move();
      }

      @Test
      @DisplayName("should touch food when head was in food")
      void test_pulse_eats_when_head_in_food(@Mock final Snake snakeMock) {
        final Coordinate headPosition = new Coordinate(1, 2);
        final Coordinate notToBeEaten = new Coordinate(3, 4);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setSnake(snakeMock);
        world.setFood(Set.of(headPosition, notToBeEaten));

        world.pulse();

        assertThat(world.getFood()).containsExactly(notToBeEaten);
      }

      @Test
      @DisplayName("should have fresh food created when head was in the last piece of food")
      void test_pulse_creates_food_when_head_in_last_food(@Mock final Snake snakeMock) {
        final Coordinate headPosition = new Coordinate(1, 2);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setSnake(snakeMock);
        world.setFood(Set.of(headPosition));

        world.pulse();

        assertThat(world.getFood())
            .doesNotContain(headPosition) // be aware that there is a slight chance of collision here
            .isNotEmpty()
            .hasSizeLessThanOrEqualTo(MAX_FOOD);
      }

      @Test
      @DisplayName("should move when head was not in food")
      void test_pulse_moves_when_head_not_in_food(@Mock final Snake snakeMock) {
        when(snakeMock.getHead()).thenReturn(new Coordinate(0, 0));
        world.setSnake(snakeMock);
        world.setFood(Set.of(new Coordinate(1, 2), new Coordinate(3, 4)));

        world.pulse();

        verify(snakeMock).move();
        verify(snakeMock, never()).grow();
      }

      @Test
      @DisplayName("should leave food untouched when head was not in food")
      void test_pulse_does_not_eat_when_head_not_in_food(@Mock final Snake snakeMock) {
        when(snakeMock.getHead()).thenReturn(new Coordinate(0, 0));
        world.setSnake(snakeMock);
        final Set<Coordinate> food = Set.of(new Coordinate(1, 2), new Coordinate(3, 4));
        world.setFood(food);

        world.pulse();

        assertThat(world.getFood()).isEqualTo(food);
      }
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


  private static class StringToCoordinateSetArgumentConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(final Object source, final Class<?> targetType) throws ArgumentConversionException {
      assertThat(targetType).as("Can only convert to String").isEqualTo(Set.class);

      final String string = ((String) source).strip();
      return string.isEmpty()
             ? emptySet()
             : convertToSet(string);
    }

    private Set<Coordinate> convertToSet(final String string) {
      final String[] elements = string.split("\\s*\\)\\s*,\\s*\\(\\s*");
      return Stream.of(elements).map(this::toCoordinate).collect(toSet());
    }

    private Coordinate toCoordinate(final String string) {
      final int[] values = toCoordinateValues(string);
      return new Coordinate(values[0], values[1]);
    }

    private int[] toCoordinateValues(final String string) {
      final String[] valueStrings = string.replaceAll("[()]", "").strip().split("\\s*,\\s*");
      return Stream.of(valueStrings).mapToInt(Integer::parseInt).toArray();
    }
  }
}
