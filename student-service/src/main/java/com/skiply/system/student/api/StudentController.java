package com.skiply.system.student.api;

import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.api.register.RegisterStudentResponse;
import com.skiply.system.student.service.StudentApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Student Management APIs", description = "The API is used to register students in the skiply system.")
public class StudentController {

    private final StudentApplicationService studentApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterStudentResponse registerStudent(@Valid @RequestBody RegisterStudentCommand registerStudentCommand) {
        log.info("Student registration request");
        return studentApplicationService.registerStudent(registerStudentCommand);
    }

}
