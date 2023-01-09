package com.skiply.system.student.infrastructure.persistence.repository;

import com.skiply.system.student.infrastructure.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {
    Optional<StudentEntity> findByStudentIdAndActive(String key, boolean b);
}
