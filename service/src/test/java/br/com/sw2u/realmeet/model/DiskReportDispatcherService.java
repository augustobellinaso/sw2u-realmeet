package br.com.sw2u.realmeet.model;

import br.com.sw2u.realmeet.report.model.GeneratedReport;
import br.com.sw2u.realmeet.service.ReportDispatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;

public class DiskReportDispatcherService extends ReportDispatcherService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DiskReportDispatcherService.class);
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    
    public DiskReportDispatcherService() {
        super(null, null);
    }
    
    @Override
    public void dispatch(GeneratedReport generatedReport) {
        var outputFile = new File(TEMP_DIR, generatedReport.getFileName());
        try {
            FileCopyUtils.copy(generatedReport.getBytes(), outputFile);
            LOGGER.info("Report saved to {}", outputFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Error saving report to: " + outputFile.getAbsolutePath(), e);
        }
    }
}
