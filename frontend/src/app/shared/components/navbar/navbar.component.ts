import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-navbar',
  template: `
    <nav class="navbar">
      <div class="logo">GameSwap</div>

      <div class="menu">
        <a routerLink="/">Inicio</a>
        <a routerLink="/explorar">Explorar</a>
        <a routerLink="/perfil">Perfil</a>
      </div>

      <div class="user">
        <span>Usuario</span>
        <button (click)="logout()">Salir</button>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem;
      background: #222;
      color: white;
      flex-wrap: wrap;
    }

    .menu {
      display: flex;
      gap: 1rem;
    }

    a {
      color: white;
      text-decoration: none;
    }

    @media (max-width: 600px) {
      .menu {
        width: 100%;
        justify-content: center;
        margin: 10px 0;
      }
    }
  `]
})
export class NavbarComponent {

  constructor(private auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}