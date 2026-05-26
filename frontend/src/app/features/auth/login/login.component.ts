import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly alertService: AlertService
  ) {}

  login() {
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

  goRegister() {
    this.router.navigate(['/register']);
  }
}
