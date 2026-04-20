import { Routes } from '@angular/router';
import { UsuariosComponent } from './features/usuarios/usuarios.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'usuarios', component: UsuariosComponent },

  {
    path: '',
    loadComponent: () =>
      import('./layout/main-layout.component')
        .then(m => m.MainLayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./features/home/home.component')
            .then(m => m.HomeComponent)
      }
    ]
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component')
        .then(m => m.LoginComponent)
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component')
        .then(m => m.RegisterComponent)
  },

  {
    path: 'perfil',
    loadComponent: () =>
      import('./features/perfil/perfil.component')
        .then(m => m.PerfilComponent)
  }
];
