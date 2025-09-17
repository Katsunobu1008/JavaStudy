package com.picsy.web;

import com.picsy.service.CommunityService;
import com.picsy.web.dto.AddMemberRequest;
import com.picsy.web.dto.CommunitySnapshotResponse;
import com.picsy.web.dto.CreateCompanyRequest;
import com.picsy.web.dto.MatrixRowUpdateRequest;
import com.picsy.web.dto.RecoveryConfigRequest;
import com.picsy.web.dto.TransactionRequest;
import com.picsy.web.dto.UpdateMemberStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityMapper mapper;

    public CommunityController(CommunityService communityService, CommunityMapper mapper) {
        this.communityService = communityService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<CommunitySnapshotResponse> getSnapshot() {
        return ResponseEntity.ok(mapper.toResponse(communityService.snapshot()));
    }

    @PostMapping("/members")
    public ResponseEntity<CommunitySnapshotResponse> addMember(@Valid @RequestBody AddMemberRequest request) {
        return ResponseEntity.ok(mapper.toResponse(
                communityService.addMember(request.name(), request.type(), request.metadata())));
    }

    @PatchMapping("/members/status")
    public ResponseEntity<CommunitySnapshotResponse> updateMemberStatus(@Valid @RequestBody UpdateMemberStatusRequest request) {
        return ResponseEntity.ok(mapper.toResponse(
                communityService.updateMemberStatus(request.memberId(), request.status())));
    }

    @PostMapping("/companies")
    public ResponseEntity<CommunitySnapshotResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return ResponseEntity.ok(mapper.toResponse(
                communityService.addCompany(request.name(), request.founders(), request.metadata())));
    }

    @PostMapping("/companies/{companyId}/virtual-dissolve")
    public ResponseEntity<CommunitySnapshotResponse> virtualDissolveCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(mapper.toResponse(communityService.virtualDissolveCompany(companyId)));
    }

    @PatchMapping("/matrix/row")
    public ResponseEntity<CommunitySnapshotResponse> updateMatrixRow(@Valid @RequestBody MatrixRowUpdateRequest request) {
        double[] row = request.values().stream().mapToDouble(Double::doubleValue).toArray();
        return ResponseEntity.ok(mapper.toResponse(
                communityService.updateEvaluationRow(request.rowIndex(), row)));
    }

    @PostMapping("/transactions")
    public ResponseEntity<CommunitySnapshotResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(mapper.toResponse(
                communityService.applyTransaction(
                        request.buyerId(),
                        request.sellerId(),
                        request.amount(),
                        request.note() == null ? "" : request.note())));
    }

    @PostMapping("/recovery")
    public ResponseEntity<CommunitySnapshotResponse> runNaturalRecovery() {
        return ResponseEntity.ok(mapper.toResponse(communityService.applyNaturalRecovery()));
    }

    @PatchMapping("/recovery/config")
    public ResponseEntity<CommunitySnapshotResponse> updateRecoveryConfig(@Valid @RequestBody RecoveryConfigRequest request) {
        Duration interval = request.intervalSeconds() == null ? null : Duration.ofSeconds(request.intervalSeconds());
        return ResponseEntity.ok(mapper.toResponse(
                communityService.updateRecoveryConfig(request.gamma(), interval)));
    }
}
