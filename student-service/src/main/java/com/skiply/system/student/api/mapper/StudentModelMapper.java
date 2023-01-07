package com.skiply.system.student.api.mapper;

import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentModelMapper {

    public Student registerStudentCommandToStudentModel(final RegisterStudentCommand command) {
        return Student.builder()
                .id(command.studentId())
                .name(command.studentName())
                .grade(command.grade())
                .mobileNumber(command.mobileNumber())
                .schoolName(command.schoolName())
                .active(true) //Since it is a new registration
                .build();
    }

}
