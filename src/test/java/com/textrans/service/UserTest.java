package com.textrans.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

  private static Stream<Arguments> provideUserData() {
    return Stream.of(
        Arguments.of(1999, 1, 1, 25),
        Arguments.of(2000, 1, 1, 24),
        Arguments.of(2001, 1, 2, 23)
    );
  }

  @ParameterizedTest
  @MethodSource("provideUserData")
  void testUserAge(int year, int month, int day, int expectedAge) {
    User user = new User();
    user.setBirthday(LocalDate.of(year, month, day));
    assertEquals(expectedAge, user.getAge());
  }
}
