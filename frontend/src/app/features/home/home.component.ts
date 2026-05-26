import { Component } from '@angular/core';
import { UsuarioService } from '../../core/services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from '../../core/services/alert.service';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params['pago'] === 'ok') {
        const cantidad = Number(params['cantidad']);

        this.usuarioService.sumarSaldo(cantidad).subscribe({
          next: (saldo) => {
            this.usuarioService.setSaldo(saldo);

            this.alertService.success('Saldo añadido correctamente');
            this.router.navigate([], {
              queryParams: {},
            });
          },
        });
      }
    });
  }
}
