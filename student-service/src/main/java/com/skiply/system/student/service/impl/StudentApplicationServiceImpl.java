package com.skiply.system.student.service.impl;

import com.skiply.system.student.api.mapper.StudentModelMapper;
import com.skiply.system.student.api.register.RegisterStudentCommand;
import com.skiply.system.student.api.register.RegisterStudentResponse;
import com.skiply.system.student.infrastructure.persistence.entity.StudentEntity;
import com.skiply.system.student.infrastructure.persistence.mapper.StudentEntityMapper;
import com.skiply.system.student.infrastructure.persistence.repository.StudentRepository;
import com.skiply.system.student.service.StudentApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentApplicationServiceImpl implements StudentApplicationService {

    private final StudentRepository studentRepository;
    private final StudentModelMapper studentModelMapper;
    private final StudentEntityMapper studentEntityMapper;

    @Override
    @Transactional
    public RegisterStudentResponse registerStudent(RegisterStudentCommand registerStudentCommand) {
        // Save to DB
        final var entity = persistToDB(registerStudentCommand);
        //Prepare response
        return new RegisterStudentResponse(entity.getStudentId());
    }

    private StudentEntity persistToDB(RegisterStudentCommand command) {
        return studentRepository.save(convertToStudentEntity(command));
    }

    private StudentEntity convertToStudentEntity(RegisterStudentCommand command) {
        return studentEntityMapper.studentModelToEntity(
                studentModelMapper.registerStudentCommandToStudentModel(command));
    }
}
