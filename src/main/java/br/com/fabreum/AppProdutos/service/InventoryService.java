package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.enums.InventoryTransactionReason;
import br.com.fabreum.AppProdutos.exception.ResourceNotFoundException;
import br.com.fabreum.AppProdutos.model.InventoryTransaction;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.InventoryTransactionRepository;
import br.com.fabreum.AppProdutos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryTransactionRepository transactionRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Adiciona uma quantidade ao estoque de um produto.
     * @Transactional garante que toda a operação seja atômica. Se algo falhar,
     * nenhuma alteração será salva no banco de dados.
     */
    @Transactional
    public Produtos addStock(Long productId, int quantity, InventoryTransactionReason reason, String referenceId) {
        Produtos product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + productId));

        product.setStockQuantity(product.getStockQuantity() + quantity);
        createTransaction(product, quantity, reason, referenceId);

        return productRepository.save(product);
    }

    /**
     * Remove uma quantidade do estoque de um produto.
     */
    @Transactional
    public Produtos removeStock(Long productId, int quantity, InventoryTransactionReason reason, String referenceId) {
        Produtos product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + product.getName());
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        createTransaction(product, -quantity, reason, referenceId); // Delta negativo

        // Lógica de aviso de estoque baixo
        if (product.getStockQuantity() <= 5) { // Limite de estoque baixo (pode ser configurável)
            System.out.println("ALERTA: Estoque baixo para o produto " + product.getName() + "! Quantidade atual: " + product.getStockQuantity());
            // Em um sistema real, isso poderia enviar um email, uma notificação, etc.
        }

        return productRepository.save(product);
    }

    public int getStockQuantity(Long productId) {
        Produtos product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + productId));
        return product.getStockQuantity();
    }

    public List<InventoryTransaction> getTransactionsForProduct(Long productId) {
        return transactionRepository.findByProductId(productId);
    }

    /**
     * Método auxiliar para criar e salvar uma transação de estoque.
     */
    private void createTransaction(Produtos product, int delta, InventoryTransactionReason reason, String referenceId) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(product.getId());
        transaction.setDelta(delta);
        transaction.setReason(reason);
        transaction.setReferenceId(referenceId);
        transaction.setCreatedBy(usuarioService.getCurrentUsername());
        transactionRepository.save(transaction);
    }
}
