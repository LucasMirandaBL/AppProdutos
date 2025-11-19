package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.controller.dto.CategoriaRequest;
import br.com.fabreum.AppProdutos.controller.dto.CategoriaResponse;
import br.com.fabreum.AppProdutos.model.Categoria;
import br.com.fabreum.AppProdutos.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para gerenciar as Categorias.
 * O acesso à maioria dos endpoints é restrito a usuários com o papel de ADMIN.
 */
@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Endpoint para criar uma nova categoria.
     * Apenas para ADMINs.
     *
     * @param request DTO com os dados da categoria.
     * @return A categoria criada.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> createCategoria(@RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.createCategoria(request);
        return ResponseEntity.ok(new CategoriaResponse(categoria));
    }

    /**
     * Endpoint para listar todas as categorias.
     * Acessível por qualquer usuário autenticado.
     *
     * @return Uma lista de categorias em formato hierárquico.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        // Filtra para pegar apenas as categorias raiz (que não têm pai)
        // O DTO CategoriaResponse cuida da montagem da hierarquia.
        List<CategoriaResponse> response = categorias.stream()
                .filter(c -> c.getParent() == null)
                .map(CategoriaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para atualizar uma categoria existente.
     * Apenas para ADMINs.
     *
     * @param id O ID da categoria a ser atualizada.
     * @param request DTO com os novos dados.
     * @return A categoria atualizada.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> updateCategoria(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.updateCategoria(id, request);
        return ResponseEntity.ok(new CategoriaResponse(categoria));
    }

    /**
     * Endpoint para deletar uma categoria.
     * Apenas para ADMINs.
     *
     * @param id O ID da categoria a ser deletada.
     * @return Resposta sem conteúdo (204 No Content).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
