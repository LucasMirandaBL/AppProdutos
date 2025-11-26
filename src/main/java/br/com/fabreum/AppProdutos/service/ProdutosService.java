package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.controller.dto.ProdutoDto;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutosService {

    private final ProductRepository produtosRepository;

    public Optional<Produtos> atualizaProduto(Long id, ProdutoDto produtoDto) {
        log.info("Atualizando produto: {}", produtoDto);
        final var produtoExistente = produtosRepository.findById(id);
        produtoExistente.ifPresent(p -> {
            p.setName(produtoDto.getName());
            p.setDescription(produtoDto.getDescription());
            p.setPrice(produtoDto.getPrice());
            p.setStockQuantity(produtoDto.getStockQuantity());
            produtosRepository.saveAndFlush(p);
        });
        return produtoExistente;
    }

}
