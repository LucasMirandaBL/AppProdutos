package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.AuditLog;
import br.com.fabreum.AppProdutos.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAuditLogs(@RequestParam String entity) {
        return ResponseEntity.ok(auditService.findByEntityType(entity));
    }
}
