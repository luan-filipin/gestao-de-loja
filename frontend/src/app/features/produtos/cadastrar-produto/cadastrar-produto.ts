import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Produto {
  nome: string;
  descricao: string;
  categoria: string;
  quantidade: number | null;
  precoPago: number | null;
  precoVenda: number | null;
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
  categorias = [{ nome: 'Geral' }];
  novaCategoria = { nome: '' };
  modalCategoriaAberto = false;
  precoPagoFormatado = '';
  precoVendaFormatado = '';
  quantidadeFormatada = '';
  resumoQuantidade = '';

  abrirModalCategoria(): void {
    this.novaCategoria = { nome: '' };
    this.modalCategoriaAberto = true;
  }

  fecharModalCategoria(): void {
    this.modalCategoriaAberto = false;
  }

  criarCategoria(): void {
    const nome = this.novaCategoria.nome.trim();
    const categoriaExistente = this.categorias.find(
      categoria => categoria.nome.toLocaleLowerCase() === nome.toLocaleLowerCase(),
    );

    if (categoriaExistente) {
      this.produto.categoria = categoriaExistente.nome;
    } else {
      const categoria = { nome };
      this.categorias.push(categoria);
      this.produto.categoria = categoria.nome;
    }

    this.fecharModalCategoria();
  }

  cadastrarProduto(): void {
    this.produtosCadastrados.unshift({ ...this.produto });
    this.produto = this.novoProduto();
    this.precoPagoFormatado = '';
    this.precoVendaFormatado = '';
    this.quantidadeFormatada = '';
    this.resumoQuantidade = '';
  }

  calcularQuantidade(valor: string): void {
    this.quantidadeFormatada = valor.replace(/[^\dXx×]/g, '').replace('×', 'x');
    const partes = this.quantidadeFormatada.toLowerCase().split('x');

    if (partes.length === 1 && partes[0]) {
      this.produto.quantidade = Number(partes[0]);
      this.resumoQuantidade = `${this.produto.quantidade} unidades`;
      return;
    }

    if (partes.length === 2 && partes[0] && partes[1]) {
      const caixas = Number(partes[0]);
      const unidadesPorCaixa = Number(partes[1]);
      this.produto.quantidade = caixas * unidadesPorCaixa;
      this.resumoQuantidade = `${caixas} ${caixas === 1 ? 'caixa' : 'caixas'} × ${unidadesPorCaixa} = ${this.produto.quantidade} unidades`;
      return;
    }

    this.produto.quantidade = null;
    this.resumoQuantidade = '';
  }

  formatarPrecoPago(valor: string): void {
    const preco = this.formatarMoedaDigitada(valor);
    this.precoPagoFormatado = preco.formatado;
    this.produto.precoPago = preco.numero;
  }

  formatarPrecoVenda(valor: string): void {
    const preco = this.formatarMoedaDigitada(valor);
    this.precoVendaFormatado = preco.formatado;
    this.produto.precoVenda = preco.numero;
  }

  completarPrecoPago(): void {
    this.precoPagoFormatado = this.formatarComDuasCasas(this.produto.precoPago);
  }

  completarPrecoVenda(): void {
    this.precoVendaFormatado = this.formatarComDuasCasas(this.produto.precoVenda);
  }

  get resumoLucro(): string {
    const custoUnitario = this.custoUnitario;
    if (custoUnitario == null || this.produto.precoVenda == null) return '';

    const lucro = this.produto.precoVenda - custoUnitario;
    const percentual = custoUnitario > 0
      ? ` (${((lucro / custoUnitario) * 100).toFixed(1).replace('.', ',')}%)`
      : '';
    return `Lucro por unidade: ${this.formatarMoedaExibicao(lucro)}${percentual}`;
  }

  get resumoCustoUnitario(): string {
    const custoUnitario = this.custoUnitario;
    return custoUnitario == null
      ? ''
      : `Custo por unidade: ${this.formatarMoedaExibicao(custoUnitario)}`;
  }

  get lucroNegativo(): boolean {
    return this.custoUnitario != null && this.produto.precoVenda != null
      && this.produto.precoVenda < this.custoUnitario;
  }

  formatarMoedaExibicao(valor: number): string {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  private formatarMoedaDigitada(valor: string): { formatado: string; numero: number | null } {
    const valorLimpo = valor.replace(/[^\d,]/g, '');

    if (!valorLimpo) {
      return { formatado: '', numero: null };
    }

    const [inteirosBrutos, decimaisBrutos = ''] = valorLimpo.split(',');
    const inteiros = inteirosBrutos.replace(/^0+(?=\d)/, '') || '0';
    const decimais = decimaisBrutos.slice(0, 2);
    const terminouComVirgula = valorLimpo.endsWith(',');

    let formatado = Number(inteiros).toLocaleString('pt-BR');
    if (terminouComVirgula || decimais) {
      formatado += `,${decimais}`;
    }

    return {
      formatado,
      numero: Number(`${inteiros}.${decimais.padEnd(2, '0')}`),
    };
  }

  private formatarComDuasCasas(valor: number | null): string {
    return valor == null
      ? ''
      : valor.toLocaleString('pt-BR', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
        });
  }

  private get custoUnitario(): number | null {
    if (this.produto.precoPago == null || !this.produto.quantidade || this.produto.quantidade <= 0) {
      return null;
    }
    return this.produto.precoPago / this.produto.quantidade;
  }

  private novoProduto(): Produto {
    return {
      nome: '', descricao: '', categoria: '',
      quantidade: null, precoPago: null, precoVenda: null, estoqueMinimo: null,
      estoqueMaximo: null, ativo: true,
    };
  }
}
