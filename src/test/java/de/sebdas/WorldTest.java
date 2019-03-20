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

  private static final Coordinate INITIAL_SNAKE_HEAD = new Coordinate(7, 5);
  private static final Coordinate[] INITIAL_SNAKE_SEGMENTS = {new Coordinate(7, 5),
                                                              new Coordinate(6, 5),
                                                              new Coordinate(5, 5)};
  private final Direction INITIAL_SNAKE_DIRECTION = Directions.right();
  private static final int MAX_FOOD = 2;

  private World world;

  @BeforeEach
  void setup() {
    world = new World();
  }

  @Nested
  @DisplayName("constructor")
  class Testing_constructor {

    @Test
    @DisplayName("should initialize the snake as expected")
    void test_constructor_snake() {
      assertThat(world.getSnake())
          .hasHeadAt(INITIAL_SNAKE_HEAD)
          .hasSegments(INITIAL_SNAKE_SEGMENTS)
          .hasDirection(INITIAL_SNAKE_DIRECTION)
          .hasNoCollision();
    }

    @Test
    @DisplayName("should initialize the food as expected")
    void test_constructor_food() {
      //noinspection Convert2MethodRef
      assertThat(world.getFood())
          .isNotEmpty()
          .hasSizeLessThanOrEqualTo(MAX_FOOD)
          .allMatch(coordinate -> isInWorldBounds(coordinate));
    }

    @Test
    @DisplayName("should initialize the world as running (not paused)")
    void test_constructor_not_paused() {
      assertThat(world.isPaused()).isFalse();
    }
  }

  private boolean isInWorldBounds(final Coordinate coordinate) {
    final int x = coordinate.getX();
    final int y = coordinate.getY();
    return 0 <= x && x < world.getWidth() &&
           0 <= y && y < world.getHeight();
  }

  @Nested
  @DisplayName("reset()")
  class Testing_reset {

    @BeforeEach
    void setup() {
      final Set<Coordinate> initialFood = Set.of(INITIAL_SNAKE_HEAD);
      world.setFood(initialFood);

      world.pulse();
      world.pulse();
      world.onDown();
      world.pulse();
      world.onLeft();
      world.pulse();
      world.onUp();
      world.pulse();
      world.togglePause();

      assertThat(world.getSnake())
          .hasHeadAt(new Coordinate(8, 5))
          .hasSegments(new Coordinate(8, 5),
                       new Coordinate(8, 6),
                       new Coordinate(9, 6),
                       new Coordinate(9, 5)
          )
          .hasDirection(Directions.up())
          .hasCollision();
      assertThat(world.getFood()).isNotEqualTo(initialFood); // be aware that there is a slight chance of collision here
      assertThat(world.isPaused()).isTrue();
    }

    @Test
    @DisplayName("should reset the snake")
    void test_reset_snake() {
      world.reset();

      assertThat(world.getSnake())
          .hasHeadAt(INITIAL_SNAKE_HEAD)
          .hasSegments(INITIAL_SNAKE_SEGMENTS)
          .hasDirection(INITIAL_SNAKE_DIRECTION)
          .hasNoCollision();
    }

    @Test
    @DisplayName("should reset the food")
    void test_reset_food() {
      world.reset();

      //noinspection Convert2MethodRef
      assertThat(world.getFood())
          .isNotEmpty()
          .hasSizeLessThanOrEqualTo(MAX_FOOD)
          .allMatch(coordinate -> isInWorldBounds(coordinate));
    }

    @Test
    @DisplayName("should reset the paused state (to not paused)")
    void test_reset_paused() {
      world.reset();

      assertThat(world.isPaused()).isFalse();
    }
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

    @RepeatedTest(5)
    @DisplayName("getFood() should return legal coordinates")
    void test_getFood() {
      final Set<Coordinate> food = world.getFood();

      //noinspection Convert2MethodRef
      assertThat(food).isNotEmpty()
                      .hasSizeLessThanOrEqualTo(MAX_FOOD)
                      .allMatch(coordinate -> isInWorldBounds(coordinate));
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

  @ParameterizedTest
  @ValueSource(strings = {"true", "false"})
  @DisplayName("noCollisionDetected() should return the respective value of the snake")
  void test_noCollisionDetected(final boolean expectedValue) {
    final Snake snakeMock = mock(Snake.class);
    when(snakeMock.noCollisionDetected()).thenReturn(expectedValue);
    world.setSnake(snakeMock);

    final boolean actualValue = world.noCollisionDetected();

    assertThat(actualValue).isEqualTo(expectedValue);
  }

  @Nested
  @DisplayName("pulse()")
  class Testing_pulse {

    @Nested
    @DisplayName("the listeners")
    class Testing_listeners {

      @Mock private InvalidationListener listenerMock;

      @BeforeEach
      void setup() {
        world.addListener(listenerMock);
      }

      @Test
      @DisplayName("should be notified when called once")
      void test_pulse_notifies() {
        world.pulse();

        verify(listenerMock).invalidated(any(World.class));
      }

      @Test
      @DisplayName("should be notified twice when called twice")
      void test_pulse_twice_notifies_twice() {
        world.pulse();
        world.pulse();

        verify(listenerMock, times(2)).invalidated(any(World.class));
      }

      @Test
      @DisplayName("should not be notified when paused")
      void test_pulse_does_not_notify_when_paused() {
        world.togglePause();

        world.pulse();

        verify(listenerMock, never()).invalidated(any(World.class));
      }
    }

    @Nested
    @DisplayName("the snake")
    class Testing_snake {

      @Mock private Snake snakeMock;

      @BeforeEach
      void setup() {
        world.setSnake(snakeMock);
      }

      @Test
      @DisplayName("should not move nor grow when paused")
      void test_pulse_does_not_move_nor_grow_when_paused() {
        world.togglePause();

        world.pulse();

        verify(snakeMock, never()).move();
        verify(snakeMock, never()).grow();
      }

      @Test
      @DisplayName("should not touch food when paused")
      void test_pulse_does_not_eat_when_paused() {
        final Set<Coordinate> food = Set.of(new Coordinate(1, 2), new Coordinate(3, 4));
        world.setFood(food);
        world.togglePause();

        world.pulse();

        assertThat(world.getFood()).isEqualTo(food);
      }

      @Test
      @DisplayName("should grow when head was in food")
      void test_pulse_grows_when_head_in_food() {
        final Coordinate headPosition = new Coordinate(1, 2);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setFood(Set.of(headPosition, new Coordinate(3, 4)));

        world.pulse();

        verify(snakeMock).grow();
        verify(snakeMock, never()).move();
      }

      @Test
      @DisplayName("should touch food when head was in food")
      void test_pulse_eats_when_head_in_food() {
        final Coordinate headPosition = new Coordinate(1, 2);
        final Coordinate notToBeEaten = new Coordinate(3, 4);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setFood(Set.of(headPosition, notToBeEaten));

        world.pulse();

        assertThat(world.getFood()).containsExactly(notToBeEaten);
      }

      @Test
      @DisplayName("should have fresh food created when head was in the last piece of food")
      void test_pulse_creates_food_when_head_in_last_food() {
        final Coordinate headPosition = new Coordinate(1, 2);
        when(snakeMock.getHead()).thenReturn(headPosition);
        world.setFood(Set.of(headPosition));

        world.pulse();

        assertThat(world.getFood())
            .doesNotContain(headPosition) // be aware that there is a slight chance of collision here
            .isNotEmpty()
            .hasSizeLessThanOrEqualTo(MAX_FOOD);
      }

      @Test
      @DisplayName("should move when head was not in food")
      void test_pulse_moves_when_head_not_in_food() {
        when(snakeMock.getHead()).thenReturn(new Coordinate(0, 0));
        world.setFood(Set.of(new Coordinate(1, 2), new Coordinate(3, 4)));

        world.pulse();

        verify(snakeMock).move();
        verify(snakeMock, never()).grow();
      }

      @Test
      @DisplayName("should leave food untouched when head was not in food")
      void test_pulse_does_not_eat_when_head_not_in_food() {
        when(snakeMock.getHead()).thenReturn(new Coordinate(0, 0));
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

    @Mock private Snake snakeMock;

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
  @DisplayName("togglePause()")
  class Testing_togglePause {

    @Test
    @DisplayName("should pause the world when called once")
    void test_togglePause_once() {
      world.togglePause();

      assertThat(world.isPaused()).isTrue();
    }

    @Test
    @DisplayName("should pause the world when called three times")
    void test_togglePause_thrice() {
      world.togglePause();
      world.togglePause();
      world.togglePause();

      assertThat(world.isPaused()).isTrue();
    }

    @Test
    @DisplayName("should result in a running world when called twice")
    void test_togglePause_twice() {
      world.togglePause();
      world.togglePause();

      assertThat(world.isPaused()).isFalse();
    }

    @Test
    @DisplayName("should result in a running world when called four times")
    void test_togglePause_four_times() {
      world.togglePause();
      world.togglePause();
      world.togglePause();
      world.togglePause();

      assertThat(world.isPaused()).isFalse();
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
