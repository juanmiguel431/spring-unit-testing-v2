package jmpc.unittesting.springdemo.models;

public enum GradeType {
  MATH(0),
  SCIENCE(1),
  HISTORY(2);

  private final Integer value;

  GradeType(Integer value) {
    this.value = value;
  }
}
