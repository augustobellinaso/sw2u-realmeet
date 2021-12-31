package br.com.sw2u.realmeet.controller;

import br.com.sw2u.realmeet.api.facade.AllocationsApi;
import br.com.sw2u.realmeet.api.model.AllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2u.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2u.realmeet.service.AllocationService;
import br.com.sw2u.realmeet.util.ResponseEntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
public class AllocationController implements AllocationsApi {
    
    private final AllocationService allocationService;
    private final Executor controllersExecutor;
    
    public AllocationController(AllocationService allocationService, Executor controllersExecutor) {
        this.allocationService = allocationService;
        this.controllersExecutor = controllersExecutor;
    }
    
    @Override
    public CompletableFuture<ResponseEntity<AllocationDTO>> createAllocation(CreateAllocationDTO createAllocationDTO) {
        return supplyAsync(() -> allocationService.createAllocation(createAllocationDTO), controllersExecutor).thenApply(
                ResponseEntityUtils::created);
    }
    
    @Override
    public CompletableFuture<ResponseEntity<Void>> deleteAllocation(Long id) {
        return runAsync(() -> allocationService.deleteAllocation(id), controllersExecutor).thenApply(ResponseEntityUtils::noContent);
    }
    
    @Override
    public CompletableFuture<ResponseEntity<Void>> updateAllocation(Long id, UpdateAllocationDTO updateAllocationDTO) {
        return runAsync(() -> allocationService.updateAllocation(id, updateAllocationDTO), controllersExecutor).thenApply(ResponseEntityUtils::noContent);
    }
    
    @Override
    public CompletableFuture<ResponseEntity<List<AllocationDTO>>> listAllocations(String employeeEmail, Long roomId, LocalDate startAt,
            LocalDate endAt, String orderBy, Integer limit, Integer page) {
        return supplyAsync(() -> allocationService.listAllocations(employeeEmail, roomId, startAt, endAt, orderBy, limit, page), controllersExecutor).thenApply(
                ResponseEntityUtils::ok);
    }
}
