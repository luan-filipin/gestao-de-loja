import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Produto {
  nome: string;
  apelido: string;
  descricao: string;
  categoria: string;
  quantidade: number | null;
  preco: number | null;
  estoqueMinimo: number | null;
  estoqueMaximo: number | null;
  ativo: boolean;
}

@Component({
  imports: [FormsModule],
  selector: 'app-cadastrar-produto',
  templateUrl: './cadastrar-produto.html',
  styleUrl: './cadastrar-produto.css',
})
export class CadastrarProduto {
  produto = this.novoProduto();
  produtosCadastrados: Produto[] = [];

  cadastrarProduto(): void {
    this.produtosCadastrados.unshift({ ...this.produto });
    this.produto = this.novoProduto();
  }

  private novoProduto(): Produto {
    return {
      nome: '', apelido: '', descricao: '', categoria: '',
      quantidade: null, preco: null, estoqueMinimo: null,
      estoqueMaximo: null, ativo: true,
    };
  }
}
