import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PostDetalleComponent } from '../modales/modalDetalle/post-detalle.component';
import { NgIf, NgFor } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { IgdbService } from '../../../core/services/igdb.service';
import { PostService } from '../../../core/services/post.service';
import { UsuarioService } from '../../../core/services/usuario.service';

@Component({
  standalone: true,
  imports: [FormsModule, NgIf, NgFor, PostDetalleComponent],
  templateUrl: './busqueda.component.html',
  styleUrls: ['./busqueda.component.css'],
})
export class BusquedaComponent implements OnInit {
  filtros = {
    videojuego: '',
    plataforma: '',
    tipo: '',
    estado: '',
  };

  loading = false;
  noResults = false;

  sugerenciasJuego: any[] = [];
  sugerenciasPlataforma: any[] = [];
  sugerenciasTipo: any[] = [];
  sugerenciasEstado: any[] = [];

  resultados: any[] = [];

  focusedJuego = false;
  focusedPlataforma = false;
  focusedTipo = false;
  focusedEstado = false;

  loadingJuego = false;
  loadingPlataforma = false;
  imagenes: { [key: number]: string | null } = {};

  tipos = [
    {
      value: '',
      label: 'Venta / Intercambio',
    },
    {
      value: 'VENTA',
      label: 'Venta',
    },
    {
      value: 'INTERCAMBIO',
      label: 'Intercambio',
    },
  ];

  estados = [
    {
      value: '',
      label: 'Cualquier estado',
    },
    {
      value: 'NUEVO',
      label: 'Nuevo',
    },
    {
      value: 'SEMINUEVO',
      label: 'Seminuevo',
    },
    {
      value: 'USADO',
      label: 'Usado',
    },
    {
      value: 'MUY_USADO',
      label: 'Muy usado',
    },
  ];

  constructor(
    private route: ActivatedRoute,
    private igdbService: IgdbService,
    private postService: PostService,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.filtros.plataforma = params['plataforma'] || '';
      this.filtros.videojuego = params['nombre'] || '';

      this.loading = true;
      this.buscar();
    });
  }

  buscarJuego() {
    if (!this.filtros.videojuego) return;

    this.loadingJuego = true;

    this.igdbService.buscarJuegos(this.filtros.videojuego).subscribe({
      next: (res) => {
        this.sugerenciasJuego = res;
        this.loadingJuego = false;
      },
      error: () => (this.loadingJuego = false),
    });
  }

  buscarPlataforma() {
    this.igdbService
      .buscarPlataformas(this.filtros.plataforma)
      .subscribe((res) => (this.sugerenciasPlataforma = res));
  }

  buscarTipos() {
    this.sugerenciasTipo = this.tipos;
  }

  buscarEstados() {
    this.sugerenciasEstado = this.estados;
  }

  seleccionarJuego(j: any) {
    this.filtros.videojuego = j.nombre;
    this.sugerenciasJuego = [];
    this.focusedJuego = false;
  }

  seleccionarPlataforma(p: any) {
    this.filtros.plataforma = p.nombre;
    this.sugerenciasPlataforma = [];
    this.focusedPlataforma = false;
  }

  seleccionarTipo(tipo: string) {
    this.filtros.tipo = tipo;
    this.focusedTipo = false;
  }

  seleccionarEstado(estado: string) {
    this.filtros.estado = estado;
    this.focusedEstado = false;
  }

  onFocusJuego() {
    this.focusedJuego = true;
  }

  onBlurJuego() {
    setTimeout(() => (this.focusedJuego = false), 150);
  }

  onFocusPlataforma() {
    this.focusedPlataforma = true;
  }

  onBlurPlataforma() {
    setTimeout(() => (this.focusedPlataforma = false), 150);
  }

  onBlurTipo() {
    setTimeout(() => {
      this.focusedTipo = false;
    }, 150);
  }

  onBlurEstado() {
    setTimeout(() => {
      this.focusedEstado = false;
    }, 150);
  }

  buscar() {
    this.loading = true;
    this.postService.buscarPosts(this.filtros).subscribe({
      next: (res) => {
        this.resultados = res;

        this.resultados.forEach((r) => {
          this.cargarImagen(r.idApi);
        });
        this.loading = false;
        if (this.resultados.length === 0) {
          this.noResults = true;
        } else {
          this.noResults = false;
        }
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      },
    });
  }

  getTipoLabel(value: string): string {
    return this.tipos.find((t) => t.value === value)?.label || '';
  }

  getEstadoLabel(value: string): string {
    return this.estados.find((e) => e.value === value)?.label || '';
  }

  modalOpen = false;
  postSeleccionado: any = null;

  abrirDetalle(post: any) {
    post.imagen = this.getImagen(post.idApi);

    if (post.idApiIntercambio) {
      if (this.imagenes[post.idApiIntercambio]) {
        post.imagenIntercambio = this.imagenes[post.idApiIntercambio];

        this.postSeleccionado = post;
        this.modalOpen = true;
      } else {
        this.igdbService.getCover(post.idApiIntercambio).subscribe((url) => {
          this.imagenes[post.idApiIntercambio] = url;

          post.imagenIntercambio = url;

          this.postSeleccionado = post;
          this.modalOpen = true;
        });

        return;
      }
    }

    this.postSeleccionado = post;
    this.modalOpen = true;
  }

  cerrarModal() {
    this.modalOpen = false;
  }

  getImagen(idApi: number): string {
    return this.imagenes[idApi] || 'no-image.png';
  }

  cargarImagen(idApi: number) {
    if (!idApi || this.imagenes[idApi]) {
      return;
    }

    this.igdbService.getCover(idApi).subscribe({
      next: (url) => {
        this.imagenes[idApi] = url;
      },
      error: () => {
        this.imagenes[idApi] = 'no-image.png';
      },
    });
  }

  onAccionRealizada() {
    this.buscar();
  }
}
