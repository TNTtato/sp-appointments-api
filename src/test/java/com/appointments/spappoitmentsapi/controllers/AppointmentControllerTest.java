package com.appointments.spappoitmentsapi.controllers;

import com.appointments.spappoitmentsapi.dto.AppointmentDTO;
import com.appointments.spappoitmentsapi.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentServiceMock;

    @InjectMocks
    private AppointmentController underTest;

    @BeforeEach
    void setUp() {
        underTest = new AppointmentController(appointmentServiceMock);
    }

    @Test
    void getNotEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();
        AppointmentDTO app1 = new AppointmentDTO();
        AppointmentDTO app2 = new AppointmentDTO();

        appointmentDTOListMocked.add(app1);
        appointmentDTOListMocked.add(app2);

        when(appointmentServiceMock.getAll()).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getList();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody()).isEqualTo(appointmentDTOListMocked);
    }

    @Test
    void getEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();

        when(appointmentServiceMock.getAll()).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getList();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void postWhenValidEntry() {
        AppointmentDTO appToBeCreated = new AppointmentDTO();
        AppointmentDTO appCreatedMock = new AppointmentDTO();

        when(appointmentServiceMock.post(appToBeCreated)).thenReturn(appCreatedMock);
        ResponseEntity<AppointmentDTO> response = underTest.post(appToBeCreated);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(appCreatedMock);
    }

    @Test
    void postWhenNoValidEntry() {
        AppointmentDTO appToBeCreated = new AppointmentDTO();

        when(appointmentServiceMock.post(appToBeCreated)).thenReturn(null);
        ResponseEntity<AppointmentDTO> response = underTest.post(appToBeCreated);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void postWhenNullEntry() {
        ResponseEntity<AppointmentDTO> response = underTest.post(null);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getExistingAppointmentByID() {
        AppointmentDTO appMock = new AppointmentDTO();

        when(appointmentServiceMock.getByID(1L)).thenReturn(appMock);
        ResponseEntity<AppointmentDTO> response = underTest.getByID(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(appMock);
    }

    @Test
    void getNonExtistingAppointmentByID() {

        when(appointmentServiceMock.getByID(999L)).thenReturn(null);
        ResponseEntity<AppointmentDTO> response = underTest.getByID(999L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void putWhenValidEntry() {
        AppointmentDTO appUpdater = new AppointmentDTO();
        AppointmentDTO appUpdatedMock = new AppointmentDTO();

        when(appointmentServiceMock.put(1L, appUpdater)).thenReturn(appUpdatedMock);
        ResponseEntity<AppointmentDTO> response = underTest.put(1L, appUpdater);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(appUpdatedMock);
    }

    @Test
    void putWhenNoValidEntry() {
        AppointmentDTO appUpdater = new AppointmentDTO();

        ResponseEntity<AppointmentDTO> response = underTest.put(1L, null);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void putWithNullEntry() {
        ResponseEntity<AppointmentDTO> response = underTest.put(null,null);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenExistingId() {

        when(appointmentServiceMock.delete(1L)).thenReturn(true); //1 being an existing id
        ResponseEntity response = underTest.delete(1L);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenNoExistingId() {

        when(appointmentServiceMock.delete(999L)).thenReturn(false); //999 being an unexisting id
        ResponseEntity response = underTest.delete(999L);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getByDateWhenNonEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();
        AppointmentDTO app1 = new AppointmentDTO();
        AppointmentDTO app2 = new AppointmentDTO();

        appointmentDTOListMocked.add(app1);
        appointmentDTOListMocked.add(app2);

        LocalDate someValidDate = LocalDate.now();

        when(appointmentServiceMock.getByDate(someValidDate)).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getByDate(someValidDate);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody()).isEqualTo(appointmentDTOListMocked);
    }

    @Test
    void getByDateWhenEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();

        LocalDate someValidDate = LocalDate.now();

        when(appointmentServiceMock.getByDate(someValidDate)).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getByDate(someValidDate);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getByAffiliateIDWhenNonEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();
        AppointmentDTO app1 = new AppointmentDTO();
        AppointmentDTO app2 = new AppointmentDTO();

        appointmentDTOListMocked.add(app1);
        appointmentDTOListMocked.add(app2);

        when(appointmentServiceMock.getByAffiliateID(1L)).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getByAffiliateID(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody()).isEqualTo(appointmentDTOListMocked);
    }

    @Test
    void getByAffiliateIDWhenEmptyList() {
        List<AppointmentDTO> appointmentDTOListMocked = new ArrayList<>();

        when(appointmentServiceMock.getByAffiliateID(1L)).thenReturn(appointmentDTOListMocked);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getByAffiliateID(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getByAffiliateIDWhenNullList() {

        when(appointmentServiceMock.getByAffiliateID(999L)).thenReturn(null);
        ResponseEntity<List<AppointmentDTO>> response = underTest.getByAffiliateID(999L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteByAffiliateId() {

        when(appointmentServiceMock.deleteByAffiliate(1L)).thenReturn(true);
        ResponseEntity<List<AppointmentDTO>> response = underTest.deleteByAffiliateOrTest(1L, null);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteByTestId() {

        when(appointmentServiceMock.deleteByTest(1L)).thenReturn(true);
        ResponseEntity<List<AppointmentDTO>> response = underTest.deleteByAffiliateOrTest(null, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenAffAndTestAreNull() {

        ResponseEntity<List<AppointmentDTO>> response = underTest.deleteByAffiliateOrTest(null, null);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteWhenAffAndTestAreNotNull() {

        ResponseEntity<List<AppointmentDTO>> response = underTest.deleteByAffiliateOrTest(1L, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
}