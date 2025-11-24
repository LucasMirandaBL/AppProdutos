package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ProductRepository productRepository;

    // Outros repositórios (Order, Promotion) seriam injetados aqui.

    public List<Produtos> getLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThanEqual(threshold);
    }

    // Métodos para outros relatórios (vendas, top produtos, etc.) seriam implementados aqui,
    // geralmente com queries customizadas nos repositórios.
}
