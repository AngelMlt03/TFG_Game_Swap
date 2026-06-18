import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';

  usernameTouched = false;
  passwordTouched = false;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly alertService: AlertService,
  ) {}

  login() {
    this.usernameTouched = true;
    this.passwordTouched = true;

    if (!this.formularioValido()) {
      return;
    }

    this.auth
      .login({
        username: this.username,
        password: this.password,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/']);
        },
        error: () => {
          this.alertService.error('Usuario o contraseña incorrectos');
        },
      });
  }

  formularioValido(): boolean {
    return this.username.trim().length > 0 && this.password.trim().length >= 4;
  }

  goRegister() {
    this.router.navigate(['/register']);
  }
}
