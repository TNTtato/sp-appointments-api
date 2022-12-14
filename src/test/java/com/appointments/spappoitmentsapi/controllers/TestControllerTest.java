package com.appointments.spappoitmentsapi.controllers;

import com.appointments.spappoitmentsapi.dto.TestDTO;
import com.appointments.spappoitmentsapi.services.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @Mock
    private TestService testServiceMock;

    @InjectMocks
    private TestController underTest;

    @BeforeEach
    void setUp() {
        underTest = new TestController(testServiceMock);
    }

    @Test
    void getNotEmptyList() {
        List<TestDTO> testDTOListMocked = new ArrayList<>();
        TestDTO testDTO1 = new TestDTO();
        TestDTO testDTO2 = new TestDTO();

        testDTOListMocked.add(testDTO1);
        testDTOListMocked.add(testDTO2);

        when(testServiceMock.getAll()).thenReturn(testDTOListMocked);

        ResponseEntity<List<TestDTO>> response = underTest.getList();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody()).isEqualTo(testDTOListMocked);
    }

    @Test
    void getEmptyList() {
        List<TestDTO> testDTOListMocked = new ArrayList<>();
        when(testServiceMock.getAll()).thenReturn(testDTOListMocked);
        ResponseEntity<List<TestDTO>> response = underTest.getList();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void postWithValidEntry() {
        TestDTO testToBeCreated = new TestDTO();
        TestDTO testCreatedMock = new TestDTO();

        when(testServiceMock.post(testToBeCreated)).thenReturn(testCreatedMock);

        ResponseEntity<TestDTO> response = underTest.post(testToBeCreated);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testCreatedMock);
    }

    @Test
    void postWithNoValidEntry() {
        TestDTO testToBeCreated = new TestDTO();

        when(testServiceMock.post(testToBeCreated)).thenReturn(null);
        ResponseEntity<TestDTO> response = underTest.post(testToBeCreated);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void postWithNullEntry() {
        ResponseEntity<TestDTO> response = underTest.post(null);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getExtistingTestByID() {
        TestDTO testMock = new TestDTO();
        when(testServiceMock.getByID(1L)).thenReturn(testMock);
        ResponseEntity<TestDTO> response = underTest.getByID(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testMock);
    }

    @Test
    void getNonExtistingTestByID() {

        when(testServiceMock.getByID(999L)).thenReturn(null);
        ResponseEntity<TestDTO> response = underTest.getByID(999L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void putWithValidEntry() {
        TestDTO testUpdater = new TestDTO();
        TestDTO testUpdatedMock = new TestDTO();

        when(testServiceMock.put(1L, testUpdater)).thenReturn(testUpdatedMock);
        ResponseEntity<TestDTO> response = underTest.put(1L, testUpdater);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testUpdatedMock);
    }

    @Test
    void putWithNoValidEntry() {
        TestDTO testUpdater = new TestDTO();

        ResponseEntity<TestDTO> response = underTest.put(null, testUpdater);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void putWithNullEntry() {
        ResponseEntity<TestDTO> response = underTest.put(null,null);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenExistingId() {

        when(testServiceMock.delete(1L)).thenReturn(true); //1 being an existing id
        ResponseEntity response = underTest.delete(1L);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenNoExistingId() {

        when(testServiceMock.delete(999L)).thenReturn(false); //999 being an unexisting id
        ResponseEntity response = underTest.delete(999L);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenTestIsReferenced() {

        when(testServiceMock.delete(1L)).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity response = underTest.delete(1L);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(HashMap.class);
    }
}