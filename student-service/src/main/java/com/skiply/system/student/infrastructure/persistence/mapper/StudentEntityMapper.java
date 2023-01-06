package com.skiply.system.student.infrastructure.persistence.mapper;

import com.skiply.system.student.domain.model.Student;
import com.skiply.system.student.infrastructure.persistence.entity.StudentEntity;

public class StudentEntityMapper {

    public StudentEntity studentModelToEntity(final Student student) {
        return StudentEntity.builder()
                .studentId(student.getStudentId())
                .studentName(student.getName())
                .grade(student.getGrade())
                .mobileNumber(student.getMobileNumber())
                .schoolName(student.getSchoolName())
                .build();
    }
}
