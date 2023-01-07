package com.skiply.system.student.api.register;


import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RegisterStudentCommand(@Schema(type = "string", defaultValue = "9898989")
                                     StudentId studentId, // Value object is validated
                                     @NotBlank @Size(max = 100)
                                     @Schema(type = "string", defaultValue = "FirstName MiddleName LastName")
                                     String studentName,
                                     @NotBlank @Size(max = 20)
                                     @Schema(type = "string", defaultValue = "Grade 1")
                                     String grade,
                                     @Schema(type = "string", defaultValue = "+971555555555")
                                     MobileNumber mobileNumber,
                                     @NotBlank @Size(max = 100)
                                     @Schema(type = "string", defaultValue = "School Name")
                                     String schoolName) {}
