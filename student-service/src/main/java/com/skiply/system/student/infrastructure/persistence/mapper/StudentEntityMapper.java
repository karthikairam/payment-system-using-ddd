package com.skiply.system.student.infrastructure.persistence.mapper;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.student.domain.model.Student;
import com.skiply.system.student.infrastructure.persistence.entity.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentEntityMapper {

    public StudentEntity studentModelToEntity(final Student student) {
        return StudentEntity.builder()
                .studentId(student.getId().value())
                .studentName(student.getName())
                .grade(student.getGrade())
                .mobileNumber(student.getMobileNumber())
                .schoolName(student.getSchoolName())
                .active(student.isActive())
                .build();
    }

    public Student entityToStudentModel(final StudentEntity studentEntity) {
        return Student.builder()
                .id(new StudentId(studentEntity.getStudentId()))
                .name(studentEntity.getStudentName())
                .grade(studentEntity.getGrade())
                .mobileNumber(studentEntity.getMobileNumber())
                .schoolName(studentEntity.getSchoolName())
                .active(studentEntity.isActive())
                .build();
    }
}
