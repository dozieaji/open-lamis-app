/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.HtsMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.User;
import org.fhi360.lamis.model.dto.HtsDto;
import org.fhi360.lamis.model.repositories.HtsRepository;
import org.fhi360.lamis.model.repositories.PatientRepository;
import org.fhi360.lamis.service.impl.HtsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * @author idris
 */
@RestController
@RequestMapping(value = "/api/hts")
@Api(tags = "HTS"  , description = " ")
public class HtsController {

    private final String ENTITY_NAME = "hts";

    private final HtsService htsService;

    private final PatientRepository patientRepository;

    private final HtsRepository htsRepository;

    private final HtsMapper htsMapper;

    public HtsController(HtsService htsService, PatientRepository patientRepository, HtsRepository htsRepository, HtsMapper htsMapper) {
        this.htsService = htsService;
        this.patientRepository = patientRepository;
        this.htsRepository = htsRepository;
        this.htsMapper = htsMapper;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveHts(@RequestBody HtsDto htsDto, HttpSession session) throws URISyntaxException {
        Long facliltyId = (Long) session.getAttribute("facilityId");
        String facilityName = (String) session.getAttribute("facilityName");
        Hts hts = htsMapper.dtoToHts(htsDto);
        User user = new User();
        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        }
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        facility.setName(facilityName);
        hts.setFacility(facility);
        hts.setUser(user);
        HtsDto result = htsService.save(hts);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateHts(@RequestBody HtsDto htsDto, HttpSession session) {
        Hts hts = htsMapper.dtoToHts(htsDto);
        Long facliltyId = (Long) session.getAttribute("facilityId");
        Long assessmentId = (Long) session.getAttribute("assessmentId");
        String facilityName = (String) session.getAttribute("facilityName");
        User user = new User();
        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        }
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        facility.setName(facilityName);
        hts.setFacility(facility);
        hts.setUser(user);
        HtsDto result = htsService.update(hts);
        session.removeAttribute("assessmentId");
        session.removeAttribute("clientCode");
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam(value = "htsId") HtsDto htsDto) {
        htsService.delete(htsDto);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/findhts", method = RequestMethod.GET)
    public ResponseEntity<?> findHts(@RequestParam(value = "htsId") long htsId) {
        return ResponseEntity.ok().body(htsService.findHts(htsId));
    }

    @RequestMapping(value = "/findClientCode", method = RequestMethod.GET)
    public ResponseEntity<?> findHtsByClientCode(@RequestParam(value = "clientCode") String clientCode
            , HttpSession session) {
        Long facliltyId = (Long) session.getAttribute("facilityId");
        Long assessmentId = (Long) session.getAttribute("assessmentId");
        String facilityName = (String) session.getAttribute("facilityName");
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        facility.setName(facilityName);
        return ResponseEntity.ok().body(htsService.findByClientCode(facility, clientCode));
    }


    @RequestMapping(value = "/htsGrid", method = RequestMethod.GET)
    public ResponseEntity<?> htsGrid(@RequestParam("rows") int rows, @RequestParam("page") int page,
                                     HttpSession session) {
        Long facliltyId = (Long) session.getAttribute("facilityId");
        Long assessmentId = (Long) session.getAttribute("assessmentId");
        String facilityName = (String) session.getAttribute("facilityName");
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        facility.setName(facilityName);
        return ResponseEntity.ok().body(htsService.htsGrid(facility, PageRequest.of(page, rows)));
    }

    @RequestMapping(value = "/findHtsByNames", method = RequestMethod.GET)
    public ResponseEntity<?> findHtsByNames(@RequestParam(value = "surname") String surname,
                                            @RequestParam(value = "otherNames") String otherNames,
                                            @RequestParam(value = "gender") String gender,
                                            @RequestParam(value = "dateBirth") LocalDate dateBirth,
                                            HttpSession session) {
        Long facliltyId = (Long) session.getAttribute("facilityId");
        Long assessmentId = (Long) session.getAttribute("assessmentId");
        String facilityName = (String) session.getAttribute("facilityName");
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        facility.setName(facilityName);
        return ResponseEntity.ok().body(htsService.findHtsByNames(facility, surname, otherNames, gender, dateBirth));
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(htsRepository.findAll());
    }

}
