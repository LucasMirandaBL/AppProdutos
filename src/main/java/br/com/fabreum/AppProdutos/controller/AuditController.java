package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.AuditLog;
import br.com.fabreum.AppProdutos.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Auditoria", description = "Endpoints for viewing audit logs")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(summary = "Busca os logs de auditoria por entidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logs de auditoria retornados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs(@RequestParam String entity) {
        return ResponseEntity.ok(auditService.findByEntityType(entity));
    }
}
