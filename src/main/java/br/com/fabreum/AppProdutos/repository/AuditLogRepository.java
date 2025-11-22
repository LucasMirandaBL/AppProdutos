package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityType(String entityType);
}
