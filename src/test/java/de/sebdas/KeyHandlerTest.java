package de.sebdas;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;

@DisplayName("KeyHandler")
@ExtendWith(MockitoExtension.class)
class KeyHandlerTest {

  @Mock
  private World worldMock;
  @Mock
  private SnakeGame snakeGameMock;
  private KeyHandler keyHandler;

  @BeforeEach
  void test_setup() {
    keyHandler = new KeyHandler(snakeGameMock);
    keyHandler.setWorld(worldMock);
  }

  @Nested
  @DisplayName("change of direction")
  class Testing_turn {

    @Test
    @DisplayName("onLeft() should work as expected")
    void test_onLeft() {
      keyHandler.onLeft();

      verify(worldMock).onLeft();
    }

    @Test
    @DisplayName("onRight() should work as expected")
    void test_onRight() {
      keyHandler.onRight();

      verify(worldMock).onRight();
    }

    @Test
    @DisplayName("onUp() should work as expected")
    void test_onUp() {
      keyHandler.onUp();

      verify(worldMock).onUp();
    }

    @Test
    @DisplayName("onDown() should work as expected")
    void test_onDown() {
      keyHandler.onDown();

      verify(worldMock).onDown();
    }
  }

  @Test
  @DisplayName("togglePause() should toggle pause on the world")
  void test_togglePause() {
    keyHandler.togglePause();

    verify(worldMock).togglePause();
  }

  @Test
  @DisplayName("restart() should toggle pause on the world and reset the game")
  void test_restart() {
    keyHandler.restart();

    verify(worldMock).togglePause();
    verify(snakeGameMock).reset();
  }

  @Test
  @DisplayName("cancel() should toggle pause on the world and exit the game")
  void test_cancel() {
    keyHandler.cancel();

    verify(worldMock).togglePause();
    verify(snakeGameMock).exit();
  }

  @Disabled("TODO: yes() - the resulting decision is never used - remove if unnecessary")
  @Test
  void test_yes() {
    fail("TODO");
  }

  @Disabled("TODO: no() - the resulting decision is never used - remove if unnecessary")
  @Test
  void test_no() {
    fail("TODO");
  }
}
