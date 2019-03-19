package de.sebdas;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static de.sebdas.GameLoop.UPDATE_INTERVAL_NANOS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("GameLoop")
@ExtendWith(MockitoExtension.class)
class GameLoopTest {

  @Mock private Painter painterMock;
  @Mock private World worldMock;
  @Mock private AnimationTimer animationTimerMock;
  private GameLoop gameLoop;

  @BeforeEach
  void setup() {
    gameLoop = new GameLoop(painterMock, gl -> animationTimerMock);
    gameLoop.setWorld(worldMock);
  }

  @Test
  @DisplayName("setWorld() should add listener")
  void test_setWorld(@Mock final World worldMock) {
    final GameLoop theGameLoop = new GameLoop(painterMock, gl -> animationTimerMock);
    theGameLoop.setWorld(worldMock);

    verify(worldMock).addListener(any(InvalidationListener.class));
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DisplayName("the animation timer created by createAnimationTimer()")
  class Testing_createAnimationTimer {

    private GameLoop gameLoopSpy;
    private AnimationTimer animationTimer;

    @BeforeEach
    void setup() {
      gameLoopSpy = spy(gameLoop);
      animationTimer = gameLoopSpy.createAnimationTimer();
    }

    @Test
    @DisplayName("should not call update() for single handler call with timeout lower than the update interval")
    void test_handle_single_call_too_low_timeout() {
      animationTimer.handle(UPDATE_INTERVAL_NANOS - 1);

      verify(gameLoopSpy, never()).update();
    }

    @Test
    @DisplayName("should not call update() for multiple handler calls with timeout lower than the update interval")
    void test_handle_multiple_calls_with_too_low_timeout() {
      animationTimer.handle(1);
      animationTimer.handle(UPDATE_INTERVAL_NANOS / 2);
      animationTimer.handle(UPDATE_INTERVAL_NANOS - 1);

      verify(gameLoopSpy, never()).update();
    }

    @ParameterizedTest
    @ValueSource(longs = {
        UPDATE_INTERVAL_NANOS,
        UPDATE_INTERVAL_NANOS + 1,
        UPDATE_INTERVAL_NANOS * 2
    })
    @DisplayName("should call update() once for single handler call with timeout greater than or equal to the update interval")
    void test_handle_single_call_with_large_enough_timeout(final long timeout) {
      animationTimer.handle(timeout);

      verify(gameLoopSpy).update();
    }

    @ParameterizedTest(name = "[{index}] ==> expected calls={0}, timeouts={1}")
    @MethodSource("provideArgumentsFor_handle")
    @DisplayName("should call update() as expected for multiple handler calls")
    void test_handle_multiple_calls(final int expectedNumberOfCalls, final List<Long> timeouts) {
      for (final long timeout : timeouts) {
        animationTimer.handle(timeout);
      }

      verify(gameLoopSpy, times(expectedNumberOfCalls)).update();
    }

    private Stream<Arguments> provideArgumentsFor_handle() {
      return Stream.of(
          Arguments.of(2, List.of(UPDATE_INTERVAL_NANOS,
                                  UPDATE_INTERVAL_NANOS * 2)
          ),
          Arguments.of(2, List.of(UPDATE_INTERVAL_NANOS,
                                  UPDATE_INTERVAL_NANOS + 1,
                                  UPDATE_INTERVAL_NANOS / 2,
                                  UPDATE_INTERVAL_NANOS * 2)
          ),
          Arguments.of(3, List.of(UPDATE_INTERVAL_NANOS,
                                  UPDATE_INTERVAL_NANOS * 2,
                                  UPDATE_INTERVAL_NANOS * 3)
          ),
          Arguments.of(3, List.of(UPDATE_INTERVAL_NANOS,
                                  UPDATE_INTERVAL_NANOS * 2,
                                  UPDATE_INTERVAL_NANOS * 100)
          ),
          Arguments.of(3, List.of(UPDATE_INTERVAL_NANOS,
                                  UPDATE_INTERVAL_NANOS * 2,
                                  UPDATE_INTERVAL_NANOS * 2 + 1,
                                  UPDATE_INTERVAL_NANOS * 100)
          )
      );
    }
  }

  @Nested
  @DisplayName("update()")
  class Testing_update {

    @Test
    @DisplayName("should send another pulse to the world when no collision was detected")
    void test_update_with_no_collision() {
      when(worldMock.noCollisionDetected()).thenReturn(true);

      gameLoop.update();

      verify(worldMock).pulse();
    }

    @Test
    @DisplayName("should cause the painter to show a warning when a collision was detected")
    void test_update_with_collision() {
      when(worldMock.noCollisionDetected()).thenReturn(false);

      gameLoop.update();

      verify(painterMock).showWarning();
    }
  }

  @Nested
  @DisplayName("start() and stop()")
  class Testing_start_and_stop {

    @Test
    @DisplayName("start() should paint the world")
    void test_start_paints_world() {
      gameLoop.start();

      verify(painterMock).paint(worldMock);
    }

    @Test
    @DisplayName("start() should start the animation timer")
    void test_start_starts_timer() {
      gameLoop.start();

      verify(animationTimerMock).start();
    }

    @Test
    @DisplayName("stop() should stop the animation timer")
    void test_stop_stops_timer() {
      gameLoop.stop();

      verify(animationTimerMock).stop();
    }
  }
}
