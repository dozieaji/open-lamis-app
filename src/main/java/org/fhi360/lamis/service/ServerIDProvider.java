package org.fhi360.lamis.service;

import org.fhi360.lamis.config.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

//@Component
public class ServerIDProvider {
    private static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static TransactionTemplate transactionTemplate = ContextProvider.getBean(TransactionTemplate.class);
    private final static Logger LOG = LoggerFactory.getLogger(ServerIDProvider.class);


    private static List<String> tables = Arrays.asList("adherehistory", "adrhistory", "anc", "chroniccare", "clinic",
            "delivery", "devolve", "dmscreenhistory", "eac", "encounter", "laboratory", "maternalfollowup",
            "motherinformation", "nigqual", "oihistory", "partnerinformation", "pharmacy", "regimenhistory",
            "statushistory", "tbscreenhistory");

    public static Long getPatientServerId(String hospitalNum, final Long facilityId) {
        String query = "select patient_id from patient where " +
                "facility_id = ? and hospital_num = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, hospitalNum);
        return getId(ids);
    }

    public static Long getScreenId(String table, LocalDate dateVisit, String description, String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = String.format("select HISTORY_ID from %s where PATIENT_ID = ? and DATE_VISIT = ? " +
                "and DESCRIPTION = ?", table);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, description);
        return getId(ids);
    }

    public static Long getLaboratoryId(String hospitalNum, LocalDate dateReported, Long labtestid, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select laboratory_id from laboratory where PATIENT_ID = ? and DATE_REPORTED = ? and LABTEST_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateReported, labtestid);
        return getId(ids);
    }

    public static Long getPatientDependantId(String table, String hospitalNum, Object dateVisit, Long facilityId) {
        String field = "date_visit";
        switch (table) {
            case "patientcasemanager":
                field = "date_assigned";
                break;
            case "nigqual":
                field = "review_period_id";
                break;
            case "delivery":
                field = "date_delivery";
                break;
            case "devolve":
                field = "date_devolved";
                break;
            case "eac":
                field = "date_eac1";
                break;
        }
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = String.format("select %s from %s d where patient_id = ? and d.%s = ? ",
                getIdColumnName(table), table, field);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit);
        return getId(ids);
    }

    public static Long getOIHistoryId(String hospitalNum, LocalDate dateVisit, String oi, Long facilityId) {
        String query = "select history_id from oihistory d where PATIENT_ID = ? and d.date_visit = ? and OI = ?";
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, oi);
        return getId(ids);
    }

    public static Long getHtsId(String clientCode, Long facilityId) {
        String query = "select hts_id from hts where facility_id = ? and client_code = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, clientCode);
        return getId(ids);
    }

    public static Long getIndexcontactId(String indexContactCode, Long facilityId) {
        String query = "select indexcontact_id from indexcontact where facility_id = ? and index_contact_code = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, indexContactCode);
        return getId(ids);
    }

    public static Long getPharmacyId(String hospitalNum, LocalDate dateVisit, Long regimenDrugId, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select PHARMACY_ID from PHARMACY where PATIENT_ID = ? and date_visit = ? and REGIMENDRUG_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, regimenDrugId);
        return getId(ids);
    }

    public static Long getStatusHistoryId(String hospitalNum, LocalDate dateCurrentStatus, String currentStatus, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select HISTORY_ID from STATUSHISTORY where PATIENT_ID = ? and DATE_CURRENT_STATUS = ? and CURRENT_STATUS = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateCurrentStatus, currentStatus);
        return getId(ids);
    }

    public static Long getRegimenId(String hospitalNum, LocalDate dateVisit, String regimenType, String regimen, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select HISTORY_ID from REGIMENHISTORY d where PATIENT_ID = ? and d.date_visit = ? and d.REGIMENTYPE = ? and d.REGIMEN = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId, dateVisit, regimenType, regimen);
        return getId(ids);
    }

    public static Long getSpecimenId(String labNo, Long facilityId) {
        String query = "select SPECIMEN_ID from SPECIMEN where FACILITY_ID = ? and LABNO = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, labNo);
        return getId(ids);
    }

    public static Long getAncId(String ancNum, Long facilityId) {
        String query = "select ANC_ID from ANC where FACILITY_ID = ? and anc_num = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, ancNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getEidId(String labNo, Long facilityId) {
        String query = "select EID_ID from EID where FACILITY_ID = ? and LABNO = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, labNo);
        return getId(ids);
    }

    public static Long getChildFollowupChildId(String referenceNum, Long facilityId) {
        String query = "select c.CHILD_ID from CHILDFOLLOWUP cf inner join CHILD c on cf.CHILD_ID = c.CHILD_ID" +
                " where c.FACILITY_ID = ? and REFERENCE_NUM";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, referenceNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getChildFollowupId(String referenceNum, LocalDate dateVisit, Long facilityId) {
        String query = "select CHILD_ID from CHILD where REFERENCE_NUM = ? and FACILITY_ID = ?";
        Long childId = jdbcTemplate.queryForObject(query, Long.class, referenceNum, facilityId);
        query = "select CHILDFOLLOWUP_ID from CHILDFOLLOWUP cf where CHILD_ID = ? and DATE_VISIT = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, childId, dateVisit);
        return getId(ids);
    }

    public static Long getChildMotherInformationId(String hospitalNum, Long facilityId) {
        String query = "select MOTHERINFORMATION_ID from MOTHERINFORMATION where FACILITY_ID = ? and " +
                "HOSPITAL_NUM = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, hospitalNum);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    public static Long getChildId(String referenceNum, Long facilityId) {
        String query = "select CHILD_ID from CHILD where FACILITY_ID = ? and reference_num = ? ";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, referenceNum);
        return getId(ids);
    }

    public static Long getMotherInformationId(String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select MOTHERINFORMATION_ID from MOTHERINFORMATION where PATIENT_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId);
        return getId(ids);
    }

    public static Long getPartnerInformationId(String hospitalNum, Long facilityId) {
        Long patientId = getPatientServerId(hospitalNum, facilityId);
        String query = "select PARTNERINFORMATION_ID from PARTNERINFORMATION p  where p.PATIENT_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, patientId);
        return getId(ids);
    }

    public static Long getCaseManagerId(Long localId, Long facilityId) {
        String query = "select CASEMANAGER_ID from casemanager where FACILITY_ID = ? and LOCAL_ID = ?";
        List<Long> ids = jdbcTemplate.queryForList(query, Long.class, facilityId, localId);
        if (ids.size() > 0 && ids.get(0) != null) {
            return ids.get(0);
        }
        return null;
    }

    private static String getIdColumnName(String table) {
        String entityId = table + "_id";
        if (table.contains("history")) {
            entityId = "history_id";
        }
        return entityId;
    }

    private static Long getId(List<Long> ids) {
        if (ids.size() > 0) {
            return ids.get(0);
        }
        return null;
    }

    static {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ContextProvider.getBean(DataSource.class));
    }
}
