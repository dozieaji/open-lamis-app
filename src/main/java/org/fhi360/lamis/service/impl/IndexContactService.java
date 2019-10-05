/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.service.impl;

import org.fhi360.lamis.controller.mapstruct.IndexContactMapper;
import org.fhi360.lamis.model.Hts;
import org.fhi360.lamis.model.IndexContact;
import org.fhi360.lamis.model.dto.IndexContactDto;
import org.fhi360.lamis.model.repositories.IndexContactRepository;
import org.fhi360.lamis.service.MonitorService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author user10
 */
@Service
@Transactional
public class IndexContactService {

    private final IndexContactRepository indexContactRepository;
    private final IndexContactMapper indexContactMapper;

    public IndexContactService(IndexContactRepository indexContactRepository, IndexContactMapper indexContactMapper) {
        this.indexContactRepository = indexContactRepository;
        this.indexContactMapper = indexContactMapper;
    }

    public IndexContactDto saveIndexContact(IndexContact indexContact) {
        Hts hts = new Hts();
        hts.setHtsId(indexContact.getHts().getHtsId());
        indexContact.setHts(hts);
        return indexContactMapper.indexContactToDto(indexContactRepository.save(indexContact));
    }

    public IndexContactDto updateIndexcontact(IndexContact indexContact) {
        Hts hts = new Hts();
        hts.setHtsId(indexContact.getHts().getHtsId());
        indexContact.setHts(hts);
        return indexContactMapper.indexContactToDto(indexContactRepository.save(indexContact));
    }

    public void delete(long indexcontactId) {
            IndexContact indexcontact = indexContactRepository.getOne(indexcontactId);
            if (indexcontact != null) {
                indexContactRepository.delete(indexcontact);
                MonitorService.logEntity("", "indexContact", 3);
            }


    }

    public IndexContactDto findIndexcontact(long indexcContactId) {
            IndexContact indexContactId = indexContactRepository.getOne(indexcContactId);
          return indexContactMapper.indexContactToDto(indexContactId);

    }



    public List<IndexContactDto> indexcontactGrid(Long htsId, Pageable pageable) {
        Hts hts = new Hts();
        hts.setHtsId(htsId);
        return indexContactMapper.indexContactToDto(indexContactRepository.findByHts(hts,pageable));
    }

}
