import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme.service';
import { UsuarioService } from './core/services/usuario.service';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(
    private readonly theme: ThemeService,
    private usuarioService: UsuarioService
  ) {}

  title = 'GameSwap';

  ngOnInit() {
    this.theme.initTheme();
    this.usuarioService.getSaldoFromBackend().subscribe(saldo => {
      this.usuarioService.setSaldo(saldo);
    });
  }
}
