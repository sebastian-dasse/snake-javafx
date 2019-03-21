package de.sebdas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.sebdas.SnakeGame.TILE_SIZE;
import static de.sebdas.SnakeGame.scale;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Painter")
@ExtendWith(MockitoExtension.class)
class PainterTest {

  @Mock
  private GraphicsContext graphicsContextMock;
  private World worldSpy;
  private Painter painterSpy;

  @BeforeEach
  void setup() {
    worldSpy = spy(new World());
    painterSpy = spy(new Painter(graphicsContextMock, TILE_SIZE, worldSpy));
  }

  @Nested
  @DisplayName("paint()")
  class Testing_paint {

    @Test
    @DisplayName("should clear the canvas")
    void test_paint_clears_canvas() {
      painterSpy.paint();

      verify(painterSpy).clearCanvas();
      verify(graphicsContextMock).fillRect(0.0, 0.0, scale(worldSpy.getWidth()), scale(worldSpy.getHeight()));
    }

    @Test
    @DisplayName("should paint the food")
    void test_paint_food() {
      painterSpy.paint();

      verify(painterSpy).paintFood();
      verify(worldSpy).getFood();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // we just want to verify that the method was called at all
    @Test
    @DisplayName("should paint the snake")
    void test_paint_snake() {
      painterSpy.paint();

      verify(painterSpy).paintSnake();
      verify(worldSpy).getSnake();
    }
  }

  @Nested
  @DisplayName("toggleWarningColor() should actually toggle")
  class Testing_toggleWarningColor {

    private Paint initialWarningColor;

    @BeforeEach
    void setup() {
      initialWarningColor = painterSpy.getWarningColor();
    }

    @Test
    @DisplayName("should change the warning color for an odd number of calls")
    void test_toggleWarningColor_odd() {
      painterSpy.toggleWarningColor();

      assertThat(painterSpy.getWarningColor()).isNotEqualTo(initialWarningColor);

      painterSpy.toggleWarningColor();
      painterSpy.toggleWarningColor();

      assertThat(painterSpy.getWarningColor()).isNotEqualTo(initialWarningColor);
    }

    @Test
    @DisplayName("should change the warning color back to the initial value for an even number of calls")
    void test_toggleWarningColor_even() {
      painterSpy.toggleWarningColor();
      painterSpy.toggleWarningColor();

      assertThat(painterSpy.getWarningColor()).isEqualTo(initialWarningColor);

      painterSpy.toggleWarningColor();
      painterSpy.toggleWarningColor();

      assertThat(painterSpy.getWarningColor()).isEqualTo(initialWarningColor);
    }
  }

  @Test
  @DisplayName("showWarning() should draw a flashing rectangle")
  void test_showWarning_twice() {
    painterSpy.showWarning();
    painterSpy.showWarning();
    painterSpy.showWarning();

    verify(graphicsContextMock, times(3)).strokeRect(0.0, 0.0, scale(worldSpy.getWidth()), scale(worldSpy.getHeight()));
    verify(painterSpy, times(3)).toggleWarningColor();
  }
}
