import { Routes } from '@angular/router';
import { CadastrarProduto } from './features/produtos/cadastrar-produto/cadastrar-produto';

export const routes: Routes = [
  {
    path: 'produtos/cadastrar',
    component: CadastrarProduto,
  },
];
