package com.skiply.system.student.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skiply.system.common.domain.model.valueobject.MobileNumber;
import com.skiply.system.common.domain.model.valueobject.StudentId;
import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.infrastructure.persistence.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
class StudentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository repository;

    @Test
    void givenValidRegistrationRequestThenSystemRegistersTheStudent() throws Exception {
        var request = RegisterStudentCommand.builder()
                .studentId(new StudentId("98989899"))
                .grade("KG1")
                .mobileNumber(new MobileNumber("+971555555555"))
                .studentName("FirstName MiddleName LastName")
                .schoolName("School Name")
                .build();

        mockMvc.perform(
                        post("/v1/students")
                                .contentType("application/json")
                                .accept("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
        ;

        var entity = repository.findById(request.studentId().value());
        Assertions.assertThat(entity.isPresent()).isTrue();
        entity.ifPresentOrElse(studentEntity -> {
            Assertions.assertThat(studentEntity.getStudentId()).isEqualTo(request.studentId().value());
            Assertions.assertThat(studentEntity.getStudentName()).isEqualTo(request.studentName());
            Assertions.assertThat(studentEntity.getSchoolName()).isEqualTo(request.schoolName());
            Assertions.assertThat(studentEntity.getMobileNumber()).isEqualTo(request.mobileNumber());
            Assertions.assertThat(studentEntity.isActive()).isTrue();
        }, () -> Assertions.fail("Entity has not saved."));
    }

}