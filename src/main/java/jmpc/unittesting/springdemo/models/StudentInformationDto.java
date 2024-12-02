package jmpc.unittesting.springdemo.models;

public class StudentInformationDto {
  private GradebookCollegeStudent student;
  private String mathAverage;
  private String scienceAverage;
  private String historyAverage;

  public StudentInformationDto() {
  }

  public StudentInformationDto(GradebookCollegeStudent student, String mathAverage, String scienceAverage, String historyAverage) {
    this.student = student;
    this.mathAverage = mathAverage;
    this.scienceAverage = scienceAverage;
    this.historyAverage = historyAverage;
  }

  public GradebookCollegeStudent getStudent() {
    return student;
  }

  public void setStudent(GradebookCollegeStudent student) {
    this.student = student;
  }

  public String getMathAverage() {
    return mathAverage;
  }

  public void setMathAverage(String mathAverage) {
    this.mathAverage = mathAverage;
  }

  public String getScienceAverage() {
    return scienceAverage;
  }

  public void setScienceAverage(String scienceAverage) {
    this.scienceAverage = scienceAverage;
  }

  public String getHistoryAverage() {
    return historyAverage;
  }

  public void setHistoryAverage(String historyAverage) {
    this.historyAverage = historyAverage;
  }
}
