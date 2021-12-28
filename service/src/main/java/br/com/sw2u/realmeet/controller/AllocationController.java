package br.com.sw2u.realmeet.controller;

import br.com.sw2u.realmeet.api.facade.AllocationsApi;
import br.com.sw2u.realmeet.api.model.AllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import br.com.sw2u.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2u.realmeet.service.AllocationService;
import br.com.sw2u.realmeet.util.ResponseEntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static java.util.concurrent.CompletableFuture.runAsync;

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
        return CompletableFuture.supplyAsync(() -> allocationService.createAllocation(createAllocationDTO), controllersExecutor).thenApply(
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
}
