package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.service.ReportService;
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
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Endpoints for generating reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Gera um relatório de produtos com baixo estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<Produtos>> getLowStockReport(@RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(reportService.getLowStockProducts(threshold));
    }

}
