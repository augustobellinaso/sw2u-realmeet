package br.com.sw2u.realmeet.controller;

import br.com.sw2u.realmeet.api.facade.ReportsApi;
import br.com.sw2u.realmeet.service.ReportCreationService;
import br.com.sw2u.realmeet.util.ResponseEntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.runAsync;

@RestController
public class ReportController implements ReportsApi {
    
    private final ReportCreationService reportCreationService;
    private final Executor controllersExecutor;
    
    public ReportController(ReportCreationService reportCreationService, Executor controllersExecutor) {
        this.reportCreationService = reportCreationService;
        this.controllersExecutor = controllersExecutor;
    }
    
    @Override
    public CompletableFuture<ResponseEntity<Void>> createAllocationReport(
            String apiKey, String email, LocalDate dateFrom, LocalDate dateTo, String reportFormat) {
        return runAsync(() -> reportCreationService.createAllocationReport(dateFrom, dateTo, email, reportFormat), controllersExecutor).thenApply(
                ResponseEntityUtils::created);
    }
}
