package org.fhi360.lamis.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatusHistoryDTO {
    private Long patientId;
    private Long historyId;
    private String currentStatus;
    private LocalDate dateCurrentStatus;
    private String outcome;
    private LocalDate agreedDate;
    private String reasonInterrupt;
    private LocalDate dateTracked;
    private String causeDeath;
}
