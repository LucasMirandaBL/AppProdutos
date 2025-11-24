package br.com.fabreum.AppProdutos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para ser lançada quando um recurso (como um Produto ou Pedido) não é encontrado.
 * A anotação @ResponseStatus faz com que o Spring retorne o status HTTP 404 (Not Found)
 * sempre que esta exceção for lançada por um controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
