/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fhi360.lamis.controller.mapstruct.PatientMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Patient;
import org.fhi360.lamis.model.dto.PatientDTO;
import org.fhi360.lamis.model.repositories.BiometricRepository;
import org.fhi360.lamis.model.repositories.FacilityRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.MonitorService;
import org.fhi360.lamis.utility.Scrambler;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author Mattae
 */
@Controller
@RequestMapping(value = "/patient")
@Api(tags = "Patient", description = " ")
@Slf4j
public class PatientController {
    private final PatientRepository patientRepository;
    private final FacilityRepository facilityRepository;
    private final PatientMapper patientMapper;
    private final BiometricRepository biometricRepository;
    private final JdbcTemplate jdbcTemplate;

    public PatientController(PatientRepository patientRepository, FacilityRepository facilityRepository,
                             PatientMapper patientMapper, BiometricRepository biometricRepository,
                             JdbcTemplate jdbcTemplate) {
        this.patientRepository = patientRepository;
        this.facilityRepository = facilityRepository;
        this.patientMapper = patientMapper;
        this.biometricRepository = biometricRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/save")
    public String savePatient(@RequestBody PatientDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("facilityId");
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientMapper.dtoToPatient(dto);
        if (patient.getCaseManager() != null && patient.getCaseManager().getCasemanagerId() == null) {
            patient.setCaseManager(null);
        }
        patient.setFacility(facility);
        if (patient.getDateBirth() == null) {
            LocalDate registrationDate = patient.getDateRegistration();
            Integer age = patient.getAge();
            String ageUnit = patient.getAgeUnit();
            ChronoUnit unit = ChronoUnit.YEARS;
            if (ageUnit.contains("month")) {
                unit = ChronoUnit.MONTHS;
            } else if (ageUnit.contains("week")) {
                unit = ChronoUnit.WEEKS;
            } else if (ageUnit.contains("day")) {
                unit = ChronoUnit.DAYS;
            }
            LocalDate dob = registrationDate.minus(age, unit);
            patient.setDateBirth(dob);
        }
        patientRepository.save(patient);
        return "patients/patients-search";
    }


    @PutMapping("/update")
    public String updatePatient(@RequestBody PatientDTO dto, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("facilityId");
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientMapper.dtoToPatient(dto);
        patient.setFacility(facility);
        if (patient.getCaseManager() != null && patient.getCaseManager().getCasemanagerId() == null) {
            patient.setCaseManager(null);
        }
        if (patient.getDateBirth() == null) {
            LocalDate registrationDate = patient.getDateRegistration();
            Integer age = patient.getAge();
            String ageUnit = patient.getAgeUnit();
            ChronoUnit unit = ChronoUnit.YEARS;
            if (ageUnit.contains("month")) {
                unit = ChronoUnit.MONTHS;
            } else if (ageUnit.contains("week")) {
                unit = ChronoUnit.WEEKS;
            } else if (ageUnit.contains("day")) {
                unit = ChronoUnit.DAYS;
            }
            LocalDate dob = registrationDate.minus(age, unit);
            patient.setDateBirth(dob);
        }
        patientRepository.save(patient);
        return "patients/patients-search";
    }


    @DeleteMapping("/delete")
    public String delete(@RequestParam("patientId") Long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        MonitorService.logEntity(patient.getHospitalNum(), "patient", 3);
        patientRepository.delete(patient);
        return "patients/patients-search";
    }

    @GetMapping("/find")
    public ResponseEntity findPatient(@RequestParam("patientId") Long patientId) {
        Patient patient = patientRepository.getOne(patientId);
        PatientDTO dto = patientMapper.patientToDto(patient);
        biometricRepository.findByPatient(patient).ifPresent(b -> dto.setBiometric(true));
        return ResponseEntity.ok(Collections.singletonList(dto));
    }

    @GetMapping("/by-number")
    public ResponseEntity findPatientByNumber(@RequestParam("hospitalNum") String num, HttpSession session) {
        Long facilityId = (Long) session.getAttribute("facilityId");
        Facility facility = facilityRepository.getOne(facilityId);
        Patient patient = patientRepository.findByFacilityAndHospitalNum(facility, num);
        PatientDTO dto = patientMapper.patientToDto(patient);
        biometricRepository.findByPatient(patient).ifPresent(b -> dto.setBiometric(true));
        return ResponseEntity.ok(Collections.singletonList(dto));
    }

    @GetMapping("/update-hospital-number")
    public ResponseEntity updateNewHospitalNumber(@RequestParam("hospitalNum") String hospitalNum, @RequestParam("newHospitalNum") String newHospitalNum,
                                                  HttpSession session) {
        Long facilityId = (Long) session.getAttribute("facilityId");
        Facility facility = facilityRepository.getOne(facilityId);
        hospitalNum = StringUtils.leftPad(hospitalNum, 7, "0");
        newHospitalNum = StringUtils.leftPad(newHospitalNum, 7, "0");
        Patient patient = patientRepository.findByFacilityAndHospitalNum(facility, hospitalNum);
        patient.setHospitalNum(newHospitalNum);
        patientRepository.save(patient);
        MonitorService.logEntity(hospitalNum + "#" + newHospitalNum, "patient", 4);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/grid")
    @ResponseBody
    public ResponseEntity patientGrid(@RequestParam(value = "rows", defaultValue = "50") Integer rows,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String female,
                                      @RequestParam(required = false) String unsuppressed, HttpSession session) {
        Long facilityId = 2L;//(Long) session.getAttribute("facilityId");
        if (page > 0) {
            page--;
        }
        System.out.println("name: " + name);
        System.out.println("female: " + female);
        String query;
        if (name != null) {
            LOG.info("1");
            Scrambler scrambler = new Scrambler();
            name = scrambler.scrambleCharacters(name);
            name = name.toUpperCase();
            if (StringUtils.isEmpty(name)) {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id " +
                        "= p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + "" +
                        " ORDER BY surname ASC LIMIT " + page + " , " + rows;
                if (female != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b " +
                            "inner join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                            facilityId + " AND gender = 'Female' ORDER BY surname ASC LIMIT " + page + " , " + rows;
                }
            } else {
                query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                        "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id " +
                        "= p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " " +
                        "AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE '" + name +
                        "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR UPPER(CONCAT(other_names, " +
                        "' ', surname)) LIKE '" + name + "%'  ORDER BY surname ASC LIMIT " + page + " , " + rows;
                if (female != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                            "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId +
                            " AND gender = 'Female' AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE " +
                            "'" + name + "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  " +
                            "OR UPPER(CONCAT(other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname " +
                            "ASC LIMIT " + page + " , " + rows;
                }
                if (unsuppressed != null) {
                    query = "SELECT p.*, (select case when count(*) > 0 then true else false end from biometric b inner " +
                            "join patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and " +
                            "x.facility_id = p.facility_id) as biometric FROM patient p WHERE facility_id = " +
                            facilityId + " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND " +
                            "last_viral_load >=1000 AND UPPER(surname) LIKE '" + name + "%' OR UPPER(other_names) LIKE " +
                            "'" + name + "%' OR UPPER(CONCAT(surname, ' ', other_names)) LIKE '" + name + "%'  OR " +
                            "UPPER(CONCAT(other_names, ' ', surname)) LIKE '" + name + "%' ORDER BY surname " +
                            "ASC LIMIT " + page + " , " + rows;
                }
            }

        } else {
            query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join " +
                    "patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                    "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " " +
                    "ORDER BY surname ASC LIMIT " + page + " , " + rows;
            if (female != null) {
                query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join" +
                        " patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                        "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId + " AND gender =" +
                        " 'Female' ORDER BY surname ASC LIMIT " + page + " , " + rows;
            }
            if (unsuppressed != null) {
                query = "SELECT p.*,(select case when count(*) > 0 then true else false end from biometric b inner join " +
                        "patient x on x.uuid = b.patient_id  where x.patient_id = p.patient_id and x.facility_id = " +
                        "p.facility_id) as biometric FROM patient p WHERE facility_id = " + facilityId +
                        " AND current_status IN ('ART Start', 'ART Restart', 'ART Transfer In') AND last_viral_load " +
                        ">=1000 ORDER BY surname ASC LIMIT " + page + " , " + rows;
            }
        }

        //System.out.println("query: "+query);
        Map<String, Object> response = new HashMap<>();
        //List<Patient> patients = patientRepository.findAll();
        LOG.info("Query: {}", query);
        List<Patient> patients = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Patient.class));
        System.out.println("patients: " + patients.toString());
        List<PatientDTO> dtos = patientMapper.patientToDto(patients).stream()
                .map(dto -> {
                    biometricRepository.findByPatient(patientMapper.dtoToPatient(dto)).ifPresent(b -> dto.setBiometric(true));
                    Boolean due = patientRepository.dueViralLoad(patientMapper.dtoToPatient(dto));
                    if (due) {
                        dto.setDueViralLoad(1);
                    }


                    return dto;
                }).collect(toList());
        response.put("patientList", dtos);
        response.put("currpage", page);
        System.out.println("DTO: " + dtos);
        System.out.println("currpage: " + page);


        return ResponseEntity.ok(response);
    }
}
