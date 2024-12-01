package jmpc.unittesting.springdemo.repositories;

import jmpc.unittesting.springdemo.models.entities.HistoryGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryGradeRepository extends JpaRepository<HistoryGrade, Integer> {
  Iterable<HistoryGrade> findHistoryGradeGradeByStudentId(int studentId);
}
