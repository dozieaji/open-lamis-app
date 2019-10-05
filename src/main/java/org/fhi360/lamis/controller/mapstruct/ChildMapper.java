package org.fhi360.lamis.controller.mapstruct;

import org.fhi360.lamis.model.Child;
import org.fhi360.lamis.model.dto.ChildDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildMapper {
    @Mappings({
            @Mapping(source = "facility.facilityId", target = "facilityId"),
            @Mapping(source = "delivery.deliveryId", target = "deliveryId"),
            @Mapping(source = "patient.patientId", target = "patientId"),
            @Mapping(source = "anc.ancId", target = "ancId"),
            @Mapping(target = "nameMother",
                    expression = "java(child.getMother().getSurname() + \" \" + child.getMother().getOtherNames())"),
            @Mapping(source = "mother.motherinformationId", target = "motherId"),
            @Mapping(source = "mother.inFacility", target = "inFacility", defaultValue = "N/A"),
            @Mapping(target = "childName", expression = "java(child.getSurname() + \" \" + child.getOtherNames())"),
            @Mapping(source = "dateBirth", target = "dateBirth", dateFormat = "MM/dd/yyyy"),
    })
    ChildDTO childToDTO(Child child);

    Child dtoToChild(ChildDTO dto);

    List<ChildDTO> childToDTO(List<Child> children);

    List<Child> dtosToChildren(List<ChildDTO> dtos);
}
