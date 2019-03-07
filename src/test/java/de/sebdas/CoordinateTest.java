package de.sebdas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CoordinateTest {

  private static final int DONT_CARE = 0;

  @Nested
  @DisplayName("getters")
  class Testing_getters {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -123, -1, 0, 1, 123, Integer.MAX_VALUE})
    @DisplayName("getX() should work as expected")
    void test_getX(final int expectedX) {
      final Coordinate coordinate = new Coordinate(expectedX, DONT_CARE);

      final int actualX = coordinate.getX();

      assertThat(actualX).isEqualTo(expectedX);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -123, -1, 0, 1, 123, Integer.MAX_VALUE})
    @DisplayName("getY() should work as expected")
    void test_getY(final int expectedY) {
      final Coordinate coordinate = new Coordinate(DONT_CARE, expectedY);

      final int actualY = coordinate.getY();

      assertThat(actualY).isEqualTo(expectedY);
    }
  }

  @Nested
  @DisplayName("equals() and hashCode()")
  class Testing_equals_and_hashCode {

    @SuppressWarnings({"EqualsWithItself", "ConstantConditions"}) // that is the whole purpose of this test
    @Test
    @DisplayName("equals() should return true for the same coordinate")
    void test_equals_identity() {
      final Coordinate coordinate = new Coordinate(123, 456);

      final boolean isEqual = coordinate.equals(coordinate);

      assertThat(isEqual).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "   0,   0,      0,     0",
        " 123,  456,    123,  456",
        "-123,  456,   -123,  456",
        "-123, -456,   -123, -456"
    })
    @DisplayName("equals() should reflexively return true for equal coordinates")
    void test_equals_equality(@CsvToCoordinateTuple final CoordinateTuple coordinates) {
      final Coordinate coordinate      = coordinates.first;
      final Coordinate equalCoordinate = coordinates.second;

      assertSoftly(softly -> {
        softly.assertThat(coordinate     .equals(equalCoordinate)).isTrue();
        softly.assertThat(equalCoordinate.equals(coordinate     )).isTrue();
      });
    }

    @ParameterizedTest
    @CsvSource({
        "   0,   0,      0,     1",
        " 123,  456,    124,  456",
        " 123,  456,    123, -456",
        " 123,  456,   -123, -456"
    })
    @DisplayName("equals() should reflexively return false for unequal coordinates")
    void test_equals_inequality(@CsvToCoordinateTuple final CoordinateTuple coordinates) {
      final Coordinate coordinate        = coordinates.first;
      final Coordinate unequalCoordinate = coordinates.second;

      assertSoftly(softly -> {
        softly.assertThat(coordinate       .equals(unequalCoordinate)).isFalse();
        softly.assertThat(unequalCoordinate.equals(coordinate       )).isFalse();
      });
    }

    @ParameterizedTest
    @CsvSource({
        "   0,   0,       0,    0",
        " 123,  456,    123,  456",
        "-123,  456,   -123,  456",
        "-123, -456,   -123, -456"
    })
    @DisplayName("hashCode() should return equal hash codes for equal coordinates")
    void test_hashCode_equality(@CsvToCoordinateTuple final CoordinateTuple coordinates) {
      final Coordinate coordinate      = coordinates.first;
      final Coordinate equalCoordinate = coordinates.second;

      final int hashCode      = coordinate.hashCode();
      final int otherHashCode = equalCoordinate.hashCode();

      assertThat(hashCode)
          .as("%s <-> %s", coordinate, equalCoordinate)
          .isEqualTo(otherHashCode);
    }

    @ParameterizedTest
    @CsvSource({
        "123, 456,    123,  457",
        "123, 456,    124,  456",
        "123, 456,   -123, -456"
    })
    @DisplayName("hashCode() should return unequal hash codes for unequal coordinates (most of the time)")
    void test_hashCode_inequality(@CsvToCoordinateTuple final CoordinateTuple coordinates) {
      final Coordinate coordinate        = coordinates.first;
      final Coordinate unequalCoordinate = coordinates.second;

      final int hashCode      = coordinate.hashCode();
      final int otherHashCode = unequalCoordinate.hashCode();

      assertThat(hashCode)
          .as("%s <-> %s", coordinate, unequalCoordinate)
          .isNotEqualTo(otherHashCode);
    }
  }

  @ParameterizedTest
  @CsvSource({
      "'Coordinate{x=0, y=0}'         ,      0,      0",
      "'Coordinate{x=0, y=1}'         ,      0,      1",
      "'Coordinate{x=1, y=0}'         ,      1,      0",
      "'Coordinate{x=1, y=1}'         ,      1,      1",
      "'Coordinate{x=-12345, y=12345}', -12345,  12345"
  })
  @DisplayName("toString() should work as expected")
  void test_toString(final String expectedString, @AggregateWith(CoordinateAggregator.class) final Coordinate coordinate) {
    final String actualString = coordinate.toString();

    assertThat(actualString).isEqualTo(expectedString);
  }


  private static class CoordinateTupleAggregator implements ArgumentsAggregator {

    @Override
    public CoordinateTuple aggregateArguments(final ArgumentsAccessor arguments, final ParameterContext context) throws ArgumentsAggregationException {
      return CoordinateTuple.of(
          new Coordinate(arguments.getInteger(0), arguments.getInteger(1)),
          new Coordinate(arguments.getInteger(2), arguments.getInteger(3))
      );
    }
  }


  private static class CoordinateTuple {
    final Coordinate first;
    final Coordinate second;

    private CoordinateTuple(final Coordinate first, final Coordinate second) {
      this.first = first;
      this.second = second;
    }

    static CoordinateTuple of(final Coordinate first, final Coordinate second) {
      return new CoordinateTuple(first, second);
    }
  }


  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  @AggregateWith(CoordinateTupleAggregator.class)
  @interface CsvToCoordinateTuple {}



  private static class CoordinateAggregator implements ArgumentsAggregator {

    @Override
    public Coordinate aggregateArguments(final ArgumentsAccessor arguments, final ParameterContext context) throws ArgumentsAggregationException {
      return new Coordinate(
          arguments.getInteger(1),
          arguments.getInteger(2));
    }
  }
}
