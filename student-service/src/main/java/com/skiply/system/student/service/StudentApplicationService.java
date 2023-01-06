package com.skiply.system.student.service;

import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.api.register.RegisterStudentResponse;

public interface StudentApplicationService {

    RegisterStudentResponse registerStudent(RegisterStudentCommand registerStudentCommand);
}
