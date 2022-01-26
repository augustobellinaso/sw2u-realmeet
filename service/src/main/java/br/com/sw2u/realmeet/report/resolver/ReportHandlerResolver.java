package br.com.sw2u.realmeet.report.resolver;

import br.com.sw2u.realmeet.report.enumeration.ReportHandlerType;
import br.com.sw2u.realmeet.report.handler.AbstractReportHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportHandlerResolver {
    
    private final Set<AbstractReportHandler> reportHandlers;
    private final Map<ReportHandlerType, AbstractReportHandler> reportHandlersMap;
    
    public ReportHandlerResolver(Set<AbstractReportHandler> reportHandlers) {
        this.reportHandlers = reportHandlers;
        reportHandlersMap = new ConcurrentHashMap<>();
    }
    
    public AbstractReportHandler resolveReportHandler(ReportHandlerType reportHandlerType) {
        return reportHandlersMap.computeIfAbsent(reportHandlerType, this::findReportHandler);
    }
    
    private AbstractReportHandler findReportHandler(ReportHandlerType reportHandlerType) {
        return reportHandlers.stream()
                .filter(reportHandler -> reportHandlerType == reportHandler.getReportHandlerType())
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Report handler not implemented for type: " + reportHandlerType.name());
    }
}
