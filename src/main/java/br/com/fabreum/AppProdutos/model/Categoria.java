package br.com.fabreum.AppProdutos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma categoria de produtos.
 * As categorias podem ter uma estrutura hierárquica (pai-filho).
 */
@Entity
@Getter
@Setter
@Table(name = "tb_categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome da categoria. Deve ser único no mesmo nível hierárquico.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Relacionamento de auto-referência para a categoria pai.
     * Uma categoria pode ter uma categoria pai, formando uma hierarquia.
     * Usamos @JsonBackReference para evitar recursão infinita na serialização JSON.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Categoria parent;

    /**
     * Lista de categorias filhas.
     * O mappedBy="parent" indica que o lado "pai" (parent) desta relação está na entidade Categoria.
     * Usamos @JsonManagedReference para evitar recursão infinita na serialização JSON.
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Categoria> children = new ArrayList<>();

    /**
     * Lista de produtos que pertencem a esta categoria.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Produtos> produtos = new ArrayList<>();

    // Construtores, getters e setters são gerenciados pelo Lombok
}
