import { Component } from '@angular/core';
import { UsuarioService } from '../../core/services/usuario.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AlertService } from '../../core/services/alert.service';
import { HomeService } from '../../core/services/home.service';
import { NgFor } from '@angular/common';
import { IgdbService } from '../../core/services/igdb.service';

import { PostDetalleComponent } from '../../shared/components/modales/modalDetalle/post-detalle.component';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [NgFor, RouterLink, PostDetalleComponent],
})
export class HomeComponent {
  ventas: any[] = [];
  intercambios: any[] = [];

  stats = {
    ventas: 0,
    intercambios: 0,
    usuarios: 0,
    reviews: 0,
  };

  topUsuarios: any[] = [];

  plataformas = [
    'PlayStation 5',
    'PlayStation 4',
    'Xbox Series X',
    'Nintendo Switch',
    'PC',
    'Xbox One',
  ];

  ultimasVentas: any[] = [];
  ultimosIntercambios: any[] = [];

  imagenes: { [key: number]: string } = {};

  modalOpen = false;
  postSeleccionado: any = null;

  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private router: Router,
    private homeService: HomeService,
    private igdbService: IgdbService,
  ) {}

  ngOnInit() {
    this.cargarHome();
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

  cargarHome() {
    this.homeService.getUltimasVentas().subscribe((r) => {
      this.ventas = r;

      r.forEach((v: any) => {
        this.cargarImagen(v.idApi);
      });
    });

    this.homeService.getUltimosIntercambios().subscribe((r) => {
      this.intercambios = r;

      r.forEach((i: any) => {
        this.cargarImagen(i.idApi);

        if (i.idApiIntercambio) {
          this.cargarImagen(i.idApiIntercambio);
        }
      });
    });

    this.homeService.getEstadisticas().subscribe((r) => (this.stats = r));

    this.homeService.getTopUsuarios().subscribe((r) => (this.topUsuarios = r));
  }

  cargarImagen(idApi: number) {
    if (this.imagenes[idApi]) return;

    this.igdbService.getCover(idApi).subscribe((url) => {
      this.imagenes[idApi] = url || 'no-image.png';
    });
  }

  getImagen(idApi: number) {
    return this.imagenes[idApi] || 'no-image.png';
  }

  abrirPost(post: any) {
    this.postSeleccionado = post;

    this.modalOpen = true;
  }

  cerrarModal() {
    this.modalOpen = false;

    this.postSeleccionado = null;
  }

  buscarPorPlataforma(plataforma: string) {
    this.router.navigate(['/busqueda'], {
      queryParams: {
        plataforma,
      },
    });
  }

  abrirPerfil(nombreUsuario: string) {
    this.router.navigate(['/usuario', nombreUsuario]);
  }
}
