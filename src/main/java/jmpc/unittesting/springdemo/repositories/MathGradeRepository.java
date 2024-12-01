package jmpc.unittesting.springdemo.repositories;

import jmpc.unittesting.springdemo.models.entities.MathGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MathGradeRepository extends JpaRepository<MathGrade, Integer> {
  Iterable<MathGrade> findMathGradeByStudentId(int studentId);
}
