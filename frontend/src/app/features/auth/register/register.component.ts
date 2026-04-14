import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="auth-container">
      <h2>Registro</h2>

      <form (ngSubmit)="register()">
        <input [(ngModel)]="name" name="name" placeholder="Nombre" required>
        <input [(ngModel)]="username" name="username" placeholder="Nombre de usuario" required>
        <input [(ngModel)]="email" name="email" type="email" placeholder="Email" required>
        <input [(ngModel)]="password" name="password" type="password" placeholder="Contraseña" required>

        <button type="submit">Registrarse</button>
      </form>

      <p (click)="goLogin()">Ya tengo una cuenta</p>
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
export class RegisterComponent {

  name = '';
  username = '';
  email = '';
  password = '';

  constructor(private auth: AuthService, private router: Router) {}

  register() {
    this.auth.register({
      name: this.name,
      username: this.username,
      correo: this.email,
      password: this.password
    }).subscribe(() => {
      this.router.navigate(['/']);
    });
  }

  goLogin() {
    this.router.navigate(['/login']);
  }
}