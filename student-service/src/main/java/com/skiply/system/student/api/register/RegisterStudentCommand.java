package com.skiply.system.student.api.register;


import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RegisterStudentCommand(StudentId studentId, // Value object is validated
                                     @NotBlank @Size(max = 100)
                                     String studentName,
                                     @NotBlank @Size(max = 20)
                                     String grade,
                                     MobileNumber mobileNumber,
                                     @NotBlank @Size(max = 100)
                                     String schoolName) {}
