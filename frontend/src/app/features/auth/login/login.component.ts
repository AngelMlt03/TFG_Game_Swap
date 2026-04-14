import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="auth-container">
      <h2>Login</h2>

      <form (ngSubmit)="login()">
        <input [(ngModel)]="username" name="username" placeholder="Usuario" required>
        <input [(ngModel)]="password" name="password" type="password" placeholder="Contraseña" required>

        <button type="submit">Entrar</button>
      </form>

      <p (click)="goRegister()">Crear cuenta</p>
    </div>
  `,
  styles: [`
    .auth-container {
      max-width: 400px;
      margin: auto;
      padding: 2rem;
    }

    input, button {
      width: 100%;
      margin: 10px 0;
      padding: 10px;
    }
  `]
})
export class LoginComponent {

  username = '';
  password = '';

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.auth.login({
      username: this.username,
      password: this.password
    }).subscribe(() => {
      this.router.navigate(['/']);
    });
  }

  goRegister() {
    this.router.navigate(['/register']);
  }
}