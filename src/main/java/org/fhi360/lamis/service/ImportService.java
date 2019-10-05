/**
 * @author aalozie
 */

package org.fhi360.lamis.service;

import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.FileUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.File;

@Component
public class ImportService {
    private final ApplicationProperties applicationProperties;
    private final XmlParserDelegator xmlParserDelegator;
    HttpSession session;
    public ImportService(ApplicationProperties applicationProperties, XmlParserDelegator xmlParserDelegator) {
        this.applicationProperties = applicationProperties;
        this.xmlParserDelegator = xmlParserDelegator;
    }


    public  synchronized void processXml() {
        String contextPath = applicationProperties.getContextPath();
        session.setAttribute("isImport", true);
        FileUtil fileUtil = new FileUtil();
        File file = null;

        String directory = contextPath + "transfer/";
        fileUtil.makeDir(directory);

        String zipFile = directory + session.getAttribute("fileName");
        Long facilityId = (Long) session.getAttribute("facilityId");
        try {
            fileUtil.unzip(zipFile);
            String[] tables = Constants.Tables.TRANSACTION_TABLES.split(",");
            for (String table : tables) {
                session.setAttribute("processingStatus", table);
                String fileName = contextPath + "exchange/" + table + ".xml";
                file = new File(fileName);
                if (file.exists()) {
                    xmlParserDelegator.delegate(table, fileName);
                    file.delete();
                }
            }
        } catch (Exception ex) {
            session.setAttribute("processingStatus", "terminated");
            ex.printStackTrace();
        }
        session.setAttribute("isImport", false);
        session.setAttribute("processingStatus", "completed");
    }
}
