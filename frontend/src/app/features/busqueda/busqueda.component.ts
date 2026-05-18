import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PostDetalleComponent } from '../../shared/components/modalDetalle/post-detalle.component';
import { NgIf, NgFor } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { IgdbService } from '../../core/services/igdb.service';
import { PostService } from '../../core/services/post.service';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  standalone: true,
  imports: [FormsModule, NgIf, NgFor, PostDetalleComponent],
  templateUrl: './busqueda.component.html',
  styleUrls: ['./busqueda.component.css']
})
export class BusquedaComponent implements OnInit {

  filtros = {
    franquicia: '', videojuego: '', plataforma: '', tipo: '', estado: ''
  };

  loading = false;

  sugerenciasJuego: any[] = [];
  sugerenciasFranquicia: any[] = [];
  sugerenciasPlataforma: any[] = [];

  resultados: any[] = [];

  franquiciaSeleccionadaId: number | null = null;

  focusedJuego = false;
  focusedFranquicia = false;
  focusedPlataforma = false;

  loadingJuego = false;
  loadingFranquicia = false;
  loadingPlataforma = false;
  imagenes: { [key: number]: string | null } = {};

  constructor(
    private route: ActivatedRoute,
    private igdbService: IgdbService,
    private postService: PostService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.filtros.plataforma = params['plataforma'] || '';
      this.filtros.videojuego = params['nombre'] || '';

      this.loading = true;
      this.buscar();
    });
  }

  buscarFranquicia() {
    this.igdbService.buscarFranquicias(this.filtros.franquicia)
      .subscribe(res => this.sugerenciasFranquicia = res);

    if (!this.filtros.franquicia) return;

    this.loadingFranquicia = true;

    this.igdbService
      .buscarFranquicias(this.filtros.franquicia)
      .subscribe({
        next: (res) => {
          this.sugerenciasFranquicia = res;
          this.loadingFranquicia = false;
        },
        error: () => this.loadingFranquicia = false
      });
  }

  buscarJuego() {
    if (!this.filtros.videojuego) return;

    this.loadingJuego = true;

    this.igdbService
      .buscarJuegos(this.filtros.videojuego, this.franquiciaSeleccionadaId || undefined)
      .subscribe({
        next: (res) => {
          this.sugerenciasJuego = res;
          this.loadingJuego = false;
        },
        error: () => this.loadingJuego = false
      });
  }

  buscarPlataforma() {
    this.igdbService.buscarPlataformas(this.filtros.plataforma)
      .subscribe(res => this.sugerenciasPlataforma = res);
  }

  seleccionarFranquicia(f: any) {
    this.filtros.franquicia = f.nombre;
    this.franquiciaSeleccionadaId = f.id;
    this.sugerenciasFranquicia = [];

    this.filtros.videojuego = '';
    this.sugerenciasJuego = [];
    this.focusedFranquicia = false;
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

  onFocusJuego() {
    this.focusedJuego = true;
  }

  onBlurJuego() {
    setTimeout(() => this.focusedJuego = false, 150);
  }

  onFocusFranquicia() {
    this.focusedFranquicia = true;
  }

  onBlurFranquicia() {
    setTimeout(() => this.focusedFranquicia = false, 150);
  }

  onFocusPlataforma() {
    this.focusedPlataforma = true;
  }

  onBlurPlataforma() {
    setTimeout(() => this.focusedPlataforma = false, 150);
  }

  buscar() {

    this.loading = true;
    this.postService.buscarPosts(this.filtros).subscribe({
      next: (res) => {
        this.resultados = res;
        
        this.resultados.forEach(r => {
          this.cargarImagen(r.idApi);
        });
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });

  }

  modalOpen = false;
  postSeleccionado: any = null;

  abrirDetalle(post: any) {
    post.imagen = this.getImagen(post.idApi);

    if (post.idApiIntercambio) {
      post.imagenIntercambio = this.getImagen(post.idApiIntercambio);
    }
    this.postSeleccionado = post;
    this.modalOpen = true;
    console.log('Post seleccionado:', post);
  }

  cerrarModal() {
    this.modalOpen = false;
  }

  getImagen(idApi: number): string {
    return this.imagenes[idApi] || 'assets/no-image.png';
  }

  cargarImagen(idApi: number) {
    this.igdbService.getCover(idApi).subscribe(url => {
      this.imagenes[idApi] = url;
      this.loading = false;
    });
  }

  onAccionRealizada() {
    this.buscar();
  }
}