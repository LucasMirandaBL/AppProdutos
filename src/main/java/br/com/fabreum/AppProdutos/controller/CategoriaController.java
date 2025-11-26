package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CategoriaRequest;
import br.com.fabreum.AppProdutos.controller.dto.CategoriaResponse;
import br.com.fabreum.AppProdutos.model.Categoria;
import br.com.fabreum.AppProdutos.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints for managing categories")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Cria uma nova categoria", description = "Apenas para ADMINs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> createCategoria(@RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.createCategoria(request);
        return ResponseEntity.ok(new CategoriaResponse(categoria));
    }

    @Operation(summary = "Lista todas as categorias", description = "Acessível por qualquer usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();

        List<CategoriaResponse> response = categorias.stream()
                .filter(c -> c.getParent() == null)
                .map(CategoriaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza uma categoria existente", description = "Apenas para ADMINs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> updateCategoria(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.updateCategoria(id, request);
        return ResponseEntity.ok(new CategoriaResponse(categoria));
    }

    @Operation(summary = "Deleta uma categoria", description = "Apenas para ADMINs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
