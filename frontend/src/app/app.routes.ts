import { Routes } from '@angular/router';
import { CadastroProdutoComponent } from
    './features/produtos/cadastro-produto/cadastro-produto';

export const routes: Routes = [
  {
    path: 'produtos/novo',
    component: CadastroProdutoComponent
  },
  {
    path: '',
    redirectTo: 'produtos/novo',
    pathMatch: 'full'
  }
];
