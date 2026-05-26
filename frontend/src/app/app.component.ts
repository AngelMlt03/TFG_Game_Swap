import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme.service';
import { UsuarioService } from './core/services/usuario.service';
import { AlertComponent } from './shared/components/alert/alert.component';
import { ConfirmDialogComponent } from './shared/components/confirm/confirm-dialog.component';
import { AlertService } from './core/services/alert.service';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [RouterOutlet, AlertComponent, ConfirmDialogComponent],
  template: `
    <router-outlet></router-outlet>
    <app-alert></app-alert>
    <app-confirm-dialog></app-confirm-dialog>
  `,
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  constructor(
    private readonly theme: ThemeService,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private route: ActivatedRoute,
  ) {}

  title = 'GameSwap';

  ngOnInit() {
    this.theme.initTheme();
    this.usuarioService.getSaldoFromBackend().subscribe((saldo) => {
      this.usuarioService.setSaldo(saldo);
    });
  }
}
