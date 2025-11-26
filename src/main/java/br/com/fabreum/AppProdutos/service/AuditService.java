package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.AuditLog;
import br.com.fabreum.AppProdutos.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void log(Object entity, String action, Object beforeState, Object afterState, String who) {
        AuditLog log = new AuditLog();
        log.setEntityType(entity.getClass().getSimpleName());

        try {
            log.setEntityId((Long) entity.getClass().getMethod("getId").invoke(entity));
        } catch (Exception e) {
            log.setEntityId(0L); // Fallback
        }

        log.setAction(action);
        log.setQuemRealizou(who);
        log.setDataHora(LocalDateTime.now());

        try {
            log.setBeforeJson(beforeState != null ? objectMapper.writeValueAsString(beforeState) : null);
            log.setAfterJson(afterState != null ? objectMapper.writeValueAsString(afterState) : null);
        } catch (JsonProcessingException e) {
            // Em um app real, logaríamos este erro de serialização.
            log.setBeforeJson("{\"error\":\"serialization failed\"}");
            log.setAfterJson("{\"error\":\"serialization failed\"}");
        }

        auditLogRepository.save(log);
    }

    public List<AuditLog> findByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }
}
