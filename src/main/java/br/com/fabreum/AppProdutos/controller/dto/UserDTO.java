package br.com.fabreum.AppProdutos.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String email;
    private String nome;
    private String password;
    private List<String> roles;
}
