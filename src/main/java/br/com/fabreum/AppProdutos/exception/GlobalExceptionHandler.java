package br.com.fabreum.AppProdutos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Um handler de exceções global. A anotação @ControllerAdvice permite que esta classe
 * intercepte exceções lançadas por qualquer @Controller na aplicação.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Lida com a ResourceNotFoundException. Retorna uma resposta HTTP 404 com uma mensagem de erro clara.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Lida com exceções de validação (ex: @NotBlank, @NotNull nos DTOs).
     * Retorna uma resposta HTTP 400 (Bad Request) com os detalhes dos campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Lida com qualquer outra exceção não tratada.
     * Retorna uma resposta HTTP 500 (Internal Server Error) genérica para não expor detalhes internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Ocorreu um erro inesperado: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
