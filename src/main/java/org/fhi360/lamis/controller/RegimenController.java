package org.fhi360.lamis.controller;

import io.swagger.annotations.Api;
import org.fhi360.lamis.model.Regimen;
import org.fhi360.lamis.model.RegimenType;
import org.fhi360.lamis.model.dto.*;
import org.fhi360.lamis.model.repositories.RegimenRepository;
import org.fhi360.lamis.model.repositories.RegimenTypeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/regimen")
@Api(tags = "Regimen" , description = " ")
public class RegimenController {

  private final   RegimenTypeRepository regimenTypeRepository;

    private final RegimenRepository regimenRepository;

    public RegimenController(RegimenTypeRepository regimenTypeRepository, RegimenRepository regimenRepository) {
        this.regimenTypeRepository = regimenTypeRepository;
        this.regimenRepository = regimenRepository;
    }

    @RequestMapping(value = "/retrieve-regimen-type-by-name", method = RequestMethod.POST)
    public ResponseEntity<List<RegimenListDto>> retrieveRegimenTypeByName(@RequestBody RegimenTypeIdDto regimenTypeIdDto, HttpSession session) {
        RegimenListDto regimenListDto = new RegimenListDto();
        List<RegimenListDto> regimenListDos = new ArrayList<>();
        RegimenType regimenType = new RegimenType();
        regimenType.setRegimenTypeId(regimenTypeIdDto.getRegimenTypeId());
        List<Regimen> regimenList = regimenRepository.findByRegimenType(regimenType);
        regimenList.forEach((regimen) -> {
            regimenListDto.setDescription(regimen.getDescription());
            regimenListDto.setRegimenId(regimen.getRegimenId());
            regimenListDto.setRegimenTypeId(regimen.getRegimenType().getRegimenTypeId());
            regimenListDto.setSelectedRegimen(regimenTypeIdDto.getSelectedRegimen());
            regimenListDos.add(regimenListDto);
        });
        session.setAttribute("regimenList", regimenListDos);
        return  ResponseEntity.ok().body(regimenListDos);
    }

    @RequestMapping(value = "/retrieve-regimen-by-id-map/{regimenTypeId}", method = RequestMethod.GET)
    public ResponseEntity<List<RegimenIdAndDescriptionDto>> retrieveRegimenByIdMap(@PathVariable(value = "regimenTypeId") Long regimenTypeId, HttpSession session) {
        RegimenIdAndDescriptionDto regimenIdAndDescriptionDto = new RegimenIdAndDescriptionDto();
        List<RegimenIdAndDescriptionDto> regimenIdAndDescriptionDtos = new ArrayList<>();
        RegimenType regimenType = new RegimenType();
        regimenType.setRegimenTypeId(regimenTypeId);
        List<Regimen> regimenList = regimenRepository.retrieveRegimenByName(regimenType);
        regimenList.forEach((regimen) -> {
            regimenIdAndDescriptionDto.setRegimenId(regimen.getRegimenId());
            regimenIdAndDescriptionDto.setDescriptions(regimen.getDescription());
            regimenIdAndDescriptionDtos.add(regimenIdAndDescriptionDto);
        });
        return  ResponseEntity.ok().body(regimenIdAndDescriptionDtos);
    }

    @RequestMapping(value = "/retrieve-regimen-type-by-name/{regimenTypeId}", method = RequestMethod.GET)
    public ResponseEntity<List<RegimenTypeDescription>> retrieveRegimenByName(@PathVariable(value = "regimenTypeId") Long regimenTypeId, HttpSession session) {
        RegimenTypeDescription regimenTypeDescription = new RegimenTypeDescription();
        List<RegimenTypeDescription> regimenTypeDescriptions = new ArrayList<>();
        RegimenType regimenType = new RegimenType();
        regimenType.setRegimenTypeId(regimenTypeId);
        List<Regimen> regimenList = regimenRepository.retrieveRegimenByName(regimenType);
        regimenList.forEach((regimen) -> {
            regimenTypeDescription.setDescriptions(regimen.getDescription());
            regimenTypeDescriptions.add(regimenTypeDescription);
        });
        return  ResponseEntity.ok().body(regimenTypeDescriptions);
    }

    @RequestMapping(value = "/retrieve-all-regimen", method = RequestMethod.GET)
    public ResponseEntity<List<RegimenIdAndDescriptionDto>> retrieveAllRegimen(HttpSession session) {
        RegimenIdAndDescriptionDto regimenIdAndDescriptionDto = new RegimenIdAndDescriptionDto();
        List<RegimenIdAndDescriptionDto> regimenIdAndDescriptionDtos = new ArrayList<>();
        List<Regimen> regimenList = regimenRepository.findAll(Sort.by(Sort.Order.by("description")));
        regimenList.forEach((regimen) -> {
            regimenIdAndDescriptionDto.setDescriptions(regimen.getDescription());
            regimenIdAndDescriptionDto.setRegimenId(regimen.getRegimenId());
            regimenIdAndDescriptionDtos.add(regimenIdAndDescriptionDto);
        });
        return  ResponseEntity.ok().body(regimenIdAndDescriptionDtos);
    }

    @RequestMapping(value = "/retrieve-regiment-type", method = RequestMethod.POST)
    public ResponseEntity<List<RegimenTypeDescription>> retrieveRegimenById(@RequestBody RegimenTypeDto regimenTypeDto, HttpSession session) {
        RegimenTypeDescription regimenTypeDescription = new RegimenTypeDescription();
        List<RegimenTypeDescription> regimenTypeDescriptions = new ArrayList<>();
        List<RegimenType> regimenTypeList = regimenTypeRepository.findAll(Sort.by(Sort.Order.by("description")));
        regimenTypeList.forEach((regimenType) -> {
            List<RegimenType> regimenTypes = null;
            if (regimenTypeDto.getRegimenType().equalsIgnoreCase("first")) {
                regimenTypes = regimenTypeRepository.firstRegimentType();
            }
            if (regimenTypeDto.getRegimenType().equalsIgnoreCase("second")) {
                regimenTypes = regimenTypeRepository.secondRegimentType();
            }
            if (regimenTypeDto.getRegimenType().equalsIgnoreCase("commence")) {
                regimenTypes = regimenTypeRepository.commenceRegimentType();
            }
            assert regimenTypes != null;
            regimenTypes.forEach((temp) -> {
                regimenTypeDescription.setDescriptions(temp.getDescription());
            });
            regimenTypeDescriptions.add(regimenTypeDescription);
        });
        return  ResponseEntity.ok().body(regimenTypeDescriptions);
    }

    }
