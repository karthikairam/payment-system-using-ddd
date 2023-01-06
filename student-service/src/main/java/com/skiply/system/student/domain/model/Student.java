package com.skiply.system.student.domain.model;

import com.skiply.system.common.domain.model.AggregateRoot;
import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
// Equals & Hashcode methods are inherited from BaseModel via AggregateRoot
public class Student extends AggregateRoot<StudentId> {

    private final StudentId studentId;
    private final String name;
    private final String grade;
    private final MobileNumber mobileNumber;
    private final String schoolName;
}
