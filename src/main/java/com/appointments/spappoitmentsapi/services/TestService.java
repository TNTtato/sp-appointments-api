package com.appointments.spappoitmentsapi.services;

import com.appointments.spappoitmentsapi.dto.TestDTO;
import com.appointments.spappoitmentsapi.entities.Test;
import com.appointments.spappoitmentsapi.repositories.TestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TestService(TestRepository testRepository, ModelMapper modelMapper) {

        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
    }

    public List<TestDTO> getAll() {
        List<Test> tests = testRepository.findAll();
        List<TestDTO> testDTOList = new ArrayList<>();
        for (Test t : tests) {
            TestDTO testDTO = modelMapper.map(t, TestDTO.class);
            testDTOList.add(testDTO);
        }
        return testDTOList;
    }

    public TestDTO post(TestDTO testDTO) {
        if (testDTO.getId() != null) {
            System.out.println("Trying to POST with an ID Field");
            return null;
        }

        if (testDTO.getName() == null || testDTO.getDescription() == null) {
            System.out.println("Missing requerided data");
            return null;
        }
        Test test = modelMapper.map(testDTO, Test.class);
        return modelMapper.map(testRepository.save(test), TestDTO.class);
    }

}
