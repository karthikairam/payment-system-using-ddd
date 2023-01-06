package com.skiply.system.student.infrastructure.persistence.entity;

import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.common.persistence.converter.MobileNumberRepoConverter;
import com.skiply.system.common.persistence.converter.StudentIdRepoConverter;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class StudentEntity {
    @Id
    @Convert(converter = StudentIdRepoConverter.class)
    @Column(name = "student_id")
    private StudentId studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "grade")
    private String grade;

    @Column(name = "mobile_number")
    @Convert(converter = MobileNumberRepoConverter.class)
    private MobileNumber mobileNumber;

    @Column(name = "school_name")
    private String schoolName;
}