package com.skiply.system.student.api;

import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.api.register.RegisterStudentResponse;
import com.skiply.system.student.service.StudentApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/students")
public class StudentController {

    private final StudentApplicationService studentApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterStudentResponse registerStudent(@Valid @RequestBody RegisterStudentCommand registerStudentCommand) {
        log.info("Student registration request");
        return studentApplicationService.registerStudent(registerStudentCommand);
    }

}
