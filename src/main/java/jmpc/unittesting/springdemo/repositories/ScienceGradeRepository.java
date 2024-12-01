package jmpc.unittesting.springdemo.repositories;

import jmpc.unittesting.springdemo.models.entities.ScienceGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceGradeRepository extends JpaRepository<ScienceGrade, Integer> {
  Iterable<ScienceGrade> findScienceGradeByStudentId(int studentId);
}
