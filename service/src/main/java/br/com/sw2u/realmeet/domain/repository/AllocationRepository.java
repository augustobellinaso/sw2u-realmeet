package br.com.sw2u.realmeet.domain.repository;

import br.com.sw2u.realmeet.domain.entity.Allocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Allocation a SET a.subject = :subject, a.startAt = :startAt, a.endAt = :endAt WHERE a.id = :allocationId")
    void updateAllocation(
            @Param("allocationId") Long allocationId, @Param("subject") String subject, @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt);
    
    @Query("SELECT a FROM Allocation a WHERE " +
            "(:employeeEmail IS NULL OR a.employee.email = :employeeEmail) AND " +
            "(:roomId IS NULL OR a.room.id = :roomId) AND " +
            "(:startAt IS NULL OR a.startAt >= :startAt) AND " +
            "(:endAt IS NULL OR a.endAt <= :endAt)")
    Page<Allocation> findAllAllocationsWithFilters(
            @Param("employeeEmail") String employeeEmail, @Param("roomId") Long roomId, @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt, Pageable pageable);
    
    @Query("SELECT a FROM Allocation a WHERE " +
            "(:employeeEmail IS NULL OR a.employee.email = :employeeEmail) AND " +
            "(:roomId IS NULL OR a.room.id = :roomId) AND " +
            "(:startAt IS NULL OR a.startAt >= :startAt) AND " +
            "(:endAt IS NULL OR a.endAt <= :endAt)")
    List<Allocation> findAllAllocationsWithFilters(
            @Param("employeeEmail") String employeeEmail, @Param("roomId") Long roomId, @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt);
}
