package com.skiply.system.student.api.register;

import com.skiply.system.common.domain.model.valueobject.StudentId;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterStudentResponse(@Schema(type = "string", defaultValue = "9898989")
                                      StudentId studentId
) {}
