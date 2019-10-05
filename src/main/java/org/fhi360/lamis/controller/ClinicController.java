/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.ClinicMapper;
import org.fhi360.lamis.model.Clinic;
import org.fhi360.lamis.model.dto.ClinicDTO;
import org.fhi360.lamis.model.dto.ServerResponse;
import org.fhi360.lamis.model.repositories.ClinicRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.impl.ClinicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author Idris
 */
@RestController
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequestMapping(value = "/api/clinic")
@Api(tags = "Clinic"  , description = " ")
public class ClinicController {
    private String ENTITY_NAME = "clinic";
    private final ClinicService clinicService;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public ClinicController(ClinicService clinicService, PatientRepository patientRepository, ClinicRepository clinicRepository, ClinicMapper clinicMapper) {
        this.clinicService = clinicService;
        this.patientRepository = patientRepository;
        this.clinicRepository = clinicRepository;
        this.clinicMapper = clinicMapper;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveClinic(@RequestBody ClinicDTO clinicDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/clinic/save/" + clinicService.save(clinicDto).getClinicId()))
                .body(clinicDto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateClinic(@RequestBody ClinicDTO clinicDto) {
        return ResponseEntity.ok().body(clinicDto);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody ClinicDTO clinicDto) {
        clinicService.delete(clinicDto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<ServerResponse> findClinic(@RequestParam(value = "patientId") long patientId,
                                                     @RequestParam(value = "dateVisit") LocalDate dateVisit) {
        return ResponseEntity.ok().body(clinicService.findClinic(patientId, dateVisit));
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public ResponseEntity findClinicById(@PathVariable Long id) {
        Clinic clinic = clinicRepository.getOne(id);
        List<ClinicDTO> dtos = clinicMapper.clinicToDto(Collections.singletonList(clinic));
        return ResponseEntity.ok().body(dtos);
    }

    @RequestMapping(value = "/findCommence", method = RequestMethod.GET)
    public ResponseEntity<ServerResponse> findCommence(@RequestParam(value = "patientId") long patientId) {
        return ResponseEntity.ok().body(clinicService.findCommence(patientId));
    }

    @RequestMapping(value = "/grid", method = RequestMethod.GET)
    public ResponseEntity<?> clinicGrid(@RequestParam("patientId") long patientId, @RequestParam("rows") int rows, @RequestParam("page") int page) {
        return ResponseEntity.ok().body(clinicService.clinicGrid(patientId, page, rows));
    }
}
