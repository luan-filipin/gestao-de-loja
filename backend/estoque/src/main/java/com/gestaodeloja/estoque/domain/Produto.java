package com.gestaodeloja.estoque.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false, name = "valor_nota_fiscal")
    private BigDecimal valorNotaFiscal;

    @Column(nullable = false, name = "preco_venda")
    private BigDecimal precoVenda;

    @Column(nullable = false, name = "estoque_minimo")
    private int estoqueMinimo;

    @Column(nullable = false, name = "estoque_maximo")
    private int estoqueMaximo;

    @Column(length = 255)
    private String descricao;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}