package jmpc.unittesting.springdemo.repositories;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<CollegeStudent, Integer> {

  CollegeStudent findByEmailAddress(String emailAddress);
}
