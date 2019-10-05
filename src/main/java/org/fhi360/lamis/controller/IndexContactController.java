/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.controller.mapstruct.IndexContactMapper;
import org.fhi360.lamis.model.Facility;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.User;
import org.fhi360.lamis.model.dto.IndexContactDto;
import org.fhi360.lamis.service.impl.IndexContactService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;

/**
 * @author Idris
 */
@RestController
@RequestMapping(value = "/indexcontact")
@Api(tags = "Index Contact"  , description = " ")
public class IndexContactController {
    private String ENTITY_NAME = "indexcontact";
    private final IndexContactService indexcontactservice;
    private IndexContactMapper indexContactMapper;

    public IndexContactController(IndexContactService indexcontactservice, IndexContactMapper indexContactMapper) {
        this.indexcontactservice = indexcontactservice;
        this.indexContactMapper = indexContactMapper;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveIndexContact(@RequestBody IndexContactDto indexContactDto, HttpSession session) throws URISyntaxException {
        Long facliltyId = (Long) session.getAttribute("facilityId");
        String facilityName = (String) session.getAttribute("facilityName");
        Facility facility = new Facility();
        facility.setFacilityId(facliltyId);
        IndexContact indexContact = indexContactMapper.indexContactDtoToindexContact(indexContactDto);
        facility.setFacilityId((Long) session.getAttribute("facilityId"));
        indexContact.setFacility(facility);

        User user = new User();
        user.setId((Long) session.getAttribute("id"));
        indexContact.setUser(user);
        indexcontactservice.saveIndexContact(indexContact);
        return ResponseEntity.ok()
                .body(indexContact);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateIndexcontact(@RequestBody IndexContactDto indexContactDto, HttpSession session) {
        Facility facility = new Facility();
        IndexContact indexContact = indexContactMapper.indexContactDtoToindexContact(indexContactDto);
        facility.setFacilityId((Long) session.getAttribute("facilityId"));
        indexContact.setFacility(facility);
        User user = new User();
        user.setId((Long) session.getAttribute("id"));
        indexContact.setUser(user);
        indexcontactservice.updateIndexcontact(indexContact);
        return ResponseEntity.ok().body(indexContact);

    }

    @RequestMapping(value = "/delete/{indexContactId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "indexContactId") Long indexContactId) {
        indexcontactservice.delete(indexContactId);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/findIndexContact/{indexcontactId}", method = RequestMethod.GET)
    public ResponseEntity<?> findIndexContact(@PathVariable(value = "indexcontactId") long indexcontactId) {
        return ResponseEntity.ok().body(indexcontactservice.findIndexcontact(indexcontactId));
    }


    @RequestMapping(value = "/index-contact-grid", method = RequestMethod.GET)
    public ResponseEntity<?> indexContactGrid(@Param("htsId") Long htsId, @Param("rows") int rows, @Param("page") int page) {
        return ResponseEntity.ok().body(indexcontactservice.indexcontactGrid(htsId, PageRequest.of(page, rows)));
    }
}
