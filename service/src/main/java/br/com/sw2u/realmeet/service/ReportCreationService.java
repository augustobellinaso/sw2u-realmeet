package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.report.enumeration.ReportFormat;
import br.com.sw2u.realmeet.report.enumeration.ReportHandlerType;
import br.com.sw2u.realmeet.report.model.AbstractReportData;
import br.com.sw2u.realmeet.report.model.AllocationReportData;
import br.com.sw2u.realmeet.report.model.GeneratedReport;
import br.com.sw2u.realmeet.report.resolver.ReportHandlerResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static br.com.sw2u.realmeet.report.enumeration.ReportHandlerType.ALLOCATION;
import static br.com.sw2u.realmeet.util.Constants.REPORT;

@Service
public class ReportCreationService {
    
    private final ReportHandlerResolver reportHandlerResolver;
    private final ReportDispatcherService reportDispatcherService;
    
    public ReportCreationService(
            ReportHandlerResolver reportHandlerResolver, ReportDispatcherService reportDispatcherService) {
        this.reportHandlerResolver = reportHandlerResolver;
        this.reportDispatcherService = reportDispatcherService;
    }
    
    @Transactional(readOnly = true)
    public void createAllocationReport(LocalDate dateFrom, LocalDate dateTo, String email, String reportFormatStr) {
        var reportData = AllocationReportData.newBuilder()
                .dateTo(dateTo)
                .dateFrom(dateFrom)
                .email(email)
                .build();
        
        createReport(ALLOCATION, email, reportFormatStr, reportData);
    }
    
    private void createReport(ReportHandlerType reportHandlerType, String email, String reportFormatStr, AbstractReportData reportData) {
        var reportFormat = ReportFormat.fromString(reportFormatStr);
        var reportHandler = reportHandlerResolver.resolveReportHandler(reportHandlerType);
        reportHandler.getReportValidator().validate(reportData);
        var bytes = reportHandler.createReportBytes(reportData, reportFormat);
        
        reportDispatcherService.dispatch(GeneratedReport
                                                 .newBuilder()
                                                 .bytes(bytes)
                                                 .reportFormat(reportFormat)
                                                 .email(email)
                                                 .fileName(buildFileName(reportHandlerType, reportFormat))
                                                 .build());
    }
    
    private String buildFileName(ReportHandlerType reportHandlerType, ReportFormat reportFormat) {
        return REPORT + reportHandlerType.name().toLowerCase() + reportFormat.getExtension();
    }
}
