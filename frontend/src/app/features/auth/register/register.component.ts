import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  name = '';
  username = '';
  email = '';
  password = '';

  nameTouched = false;
  usernameTouched = false;
  emailTouched = false;
  passwordTouched = false;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
  ) {}

  register() {
    this.nameTouched = true;
    this.usernameTouched = true;
    this.emailTouched = true;
    this.passwordTouched = true;

    if (!this.formularioValido()) {
      return;
    }

    this.auth
      .register({
        name: this.name,
        username: this.username,
        correo: this.email,
        password: this.password,
      })
      .subscribe(() => {
        this.router.navigate(['/']);
      });
  }

  esEmailValido(): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
  }

  formularioValido(): boolean {
    return (
      this.name.trim().length > 0 &&
      this.username.trim().length >= 4 &&
      this.email.trim().length > 0 &&
      this.esEmailValido() &&
      this.password.trim().length >= 8
    );
  }

  goLogin() {
    this.router.navigate(['/login']);
  }
}
