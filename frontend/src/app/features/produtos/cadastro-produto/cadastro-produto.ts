
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

@Component({
  selector: 'app-cadastro-produto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro-produto.html',
  styleUrl: './cadastro-produto.css'
})
export class CadastroProdutoComponent {
  mensagem = '';
  tipoMensagem: 'sucesso' | 'erro' = 'sucesso';
  salvando = false;

  categorias = [
    'Informática',
    'Eletrônicos',
    'Alimentos',
    'Vestuário',
    'Escritório',
    'Outros'
  ];

  produtoForm;

  constructor(private fb: FormBuilder) {
    this.produtoForm = this.fb.nonNullable.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      sku: ['', Validators.required],
      categoria: ['', Validators.required],
      fornecedor: [''],
      precoCusto: [0, [Validators.required, Validators.min(0.01)]],
      precoVenda: [0, [Validators.required, Validators.min(0.01)]],
      quantidade: [0, [Validators.required, Validators.min(0)]],
      estoqueMinimo: [5, [Validators.required, Validators.min(0)]],
      descricao: ['']
    });
  }

  get margemLucro(): number {
    const { precoCusto, precoVenda } =
      this.produtoForm.getRawValue();

    if (precoCusto <= 0) {
      return 0;
    }

    return ((precoVenda - precoCusto) / precoCusto) * 100;
  }

  salvarProduto(): void {
    this.mensagem = '';

    if (this.produtoForm.invalid) {
      this.produtoForm.markAllAsTouched();
      this.tipoMensagem = 'erro';
      this.mensagem = 'Preencha corretamente todos os campos obrigatórios.';
      return;
    }

    const produto = this.produtoForm.getRawValue();

    // Demonstração: exibe os dados no console.
    // Substitua pela chamada HTTP à sua API.
    console.log('Produto cadastrado:', produto);

    this.tipoMensagem = 'sucesso';
    this.mensagem = 'Produto validado e pronto para ser integrado ao estoque!';

    console.log('Margem de lucro:', this.margemLucro.toFixed(2) + '%');
  }

  cancelar(): void {
    this.produtoForm.reset({
      nome: '',
      sku: '',
      categoria: '',
      fornecedor: '',
      precoCusto: 0,
      precoVenda: 0,
      quantidade: 0,
      estoqueMinimo: 5,
      descricao: ''
    });

    this.mensagem = '';
  }

  campoInvalido(campo: string): boolean {
    const controle = this.produtoForm.get(campo);

    return !!controle &&
      controle.invalid &&
      controle.touched;
  }
}
