package com.skiply.system.student.infrastructure.persistence.entity;

import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.persistence.converter.MobileNumberRepoConverter;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
@Entity
public class StudentEntity {

    @Id
    @Column(name = "student_id")
    /*
       This ValueObject (Java Record) class cannot be used in a Primary key (ID) field.
       Because @Id & @Convert does not work in Hibernate. @Convert works only with normal columns like "mobileNumber".
       Also, we can't use @EmbeddedId as Hibernate requires a NoArgConstructor that is against value objects pattern.
       Moreover, it pollutes the ValueObjects with JPA annotations.
       So, I am simply went with primitive String type here.
     */
    private String studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "grade")
    private String grade;

    @Column(name = "mobile_number")
    @Convert(converter = MobileNumberRepoConverter.class)
    private MobileNumber mobileNumber;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "active")
    private boolean active;
}