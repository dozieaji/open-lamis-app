package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatusListDTO extends ListDTO {
    private List<StatusHistoryDTO> statusList;
}
