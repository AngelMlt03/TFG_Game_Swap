import { Routes } from '@angular/router';
import { UsuariosComponent } from './features/usuarios/usuarios.component';
import { authGuard } from './core/guards/auth.guard';
import { PerfilComponent } from './shared/components/perfil/perfil.component';

export const routes: Routes = [
  { path: 'usuarios', component: UsuariosComponent },

  {
    path: '',
    loadComponent: () =>
      import('./layout/main-layout.component').then(
        (m) => m.MainLayoutComponent,
      ),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./features/home/home.component').then((m) => m.HomeComponent),
      },
      {
        path: 'busqueda',
        loadComponent: () =>
          import('./shared/components/busqueda/busqueda.component').then(
            (m) => m.BusquedaComponent,
          ),
      },
    ],
  },

  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(
        (m) => m.LoginComponent,
      ),
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then(
        (m) => m.RegisterComponent,
      ),
  },

  {
    path: 'perfil',
    loadComponent: () =>
      import('./shared/components/perfil/perfil.component').then(
        (m) => m.PerfilComponent,
      ),
  },

  {
    path: 'usuario/:nombreUsuario',
    component: PerfilComponent,
  },

  {
    path: 'saldo',
    loadComponent: () =>
      import('./features/saldo/saldo.component').then((m) => m.SaldoComponent),
  },
];
