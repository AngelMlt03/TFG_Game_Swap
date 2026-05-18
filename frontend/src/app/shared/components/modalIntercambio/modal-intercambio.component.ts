import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges
} from '@angular/core';

import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ModalComponent } from '../modal/modal.component';
import { IgdbService } from '../../../core/services/igdb.service';
import { PostService } from '../../../core/services/post.service';

@Component({
  selector: 'app-modal-intercambio',
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    FormsModule,
    ModalComponent
  ],
  templateUrl: './modal-intercambio.component.html',
  styleUrls: ['../modalVenta/modal-venta-intercambio.component.css']
})
export class ModalIntercambioComponent implements OnChanges {

  @Input() isOpen = false;
  @Output() close = new EventEmitter<boolean>();
  @Input() modoEdicion = false;
  @Input() postEditar: any = null;

  nombreJuego = '';
  plataforma = '';
  estado = '';

  juegoSeleccionado: any = null;
  imagen: string | null = null;

  resultadosBusqueda: any[] = [];
  resultadosPlataforma: any[] = [];

  nombreJuegoIntercambio = '';
  plataformaIntercambio = '';
  estadoIntercambio = '';

  juegoIntercambioSeleccionado: any = null;
  imagenIntercambio: string | null = null;

  resultadosBusquedaIntercambio: any[] = [];
  resultadosPlataformaIntercambio: any[] = [];

  descripcion = '';

  focusedJuego = false;
  focusedJuegoIntercambio = false;
  focusedPlataforma = false;
  focusedPlataformaIntercambio = false;

  loadingJuego = false;
  loadingJuegoIntercambio = false;
  loadingPlataforma = false;
  loadingPlataformaIntercambio = false;

  sugerenciasPlataforma: any[] = [];
  sugerenciasPlataformaIntercambio: any[] = [];

  constructor(
    private igdbService: IgdbService,
    private postService: PostService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['postEditar'] && this.postEditar) {

      // TU JUEGO
      this.nombreJuego = this.postEditar.nombreProducto;
      this.plataforma = this.postEditar.plataforma;
      this.estado = this.postEditar.estado;
      this.imagen = this.postEditar.imagen;
      this.juegoSeleccionado = {
        id: this.postEditar.idApi,
        nombre: this.postEditar.nombreProducto
      };

      // JUEGO INTERCAMBIO
      this.nombreJuegoIntercambio = this.postEditar.nombreProductoIntercambio;
      this.plataformaIntercambio = this.postEditar.plataformaIntercambio;
      this.estadoIntercambio = this.postEditar.estadoIntercambio;
      this.imagenIntercambio = this.postEditar.imagenIntercambio;
      this.juegoIntercambioSeleccionado = {
        id: this.postEditar.idApiIntercambio,
        nombre: this.postEditar.nombreProductoIntercambio
      };

      this.descripcion = this.postEditar.descripcion || '';
    }
  }

  buscarJuego() {

    if (this.nombreJuego.length < 3) return;

    this.igdbService
      .buscarJuegos(this.nombreJuego)
      .subscribe(res => {
        this.resultadosBusqueda = res;
      });
  }

  seleccionarJuego(juego: any) {

    this.juegoSeleccionado = juego;

    this.nombreJuego = juego.nombre;
    this.imagen = juego.imagen;

    this.resultadosBusqueda = [];
  }

  buscarPlataforma() {

    if (!this.plataforma) {
      this.sugerenciasPlataforma = [];
      return;
    }

    this.loadingPlataforma = true;

    this.igdbService.buscarPlataformas(this.plataforma)
      .subscribe({
        next: res => {
          this.sugerenciasPlataforma = res;
          this.loadingPlataforma = false;
        },
        error: () => this.loadingPlataforma = false
      });
  }

  seleccionarPlataforma(p: any) {
    this.plataforma = p.nombre;
    this.focusedPlataforma = false;
    this.sugerenciasPlataforma = [];
  }

  buscarJuegoIntercambio() {

    if (this.nombreJuegoIntercambio.length < 3) return;

    this.igdbService
      .buscarJuegos(this.nombreJuegoIntercambio)
      .subscribe(res => {
        this.resultadosBusquedaIntercambio = res;
      });
  }

  seleccionarJuegoIntercambio(juego: any) {

    this.juegoIntercambioSeleccionado = juego;

    this.nombreJuegoIntercambio = juego.nombre;
    this.imagenIntercambio = juego.imagen;

    this.resultadosBusquedaIntercambio = [];
  }

  buscarPlataformaIntercambio() {

    if (this.plataformaIntercambio.length < 2) return;

    if (!this.plataformaIntercambio) {
      this.sugerenciasPlataformaIntercambio = [];
      return;
    }

    this.loadingPlataformaIntercambio = true;
    this.igdbService
      .buscarPlataformas(this.plataformaIntercambio)
      .subscribe({
        next: res => {
          this.sugerenciasPlataformaIntercambio = res;
          this.loadingPlataformaIntercambio = false;
        },
        error: () => this.loadingPlataformaIntercambio = false
      });
  }

  seleccionarPlataformaIntercambio(p: any) {

    this.plataformaIntercambio = p.nombre;
    this.focusedPlataformaIntercambio = false;
    this.sugerenciasPlataformaIntercambio = [];
  }

  formularioValido(): boolean {

    return !!(
      this.juegoSeleccionado &&
      this.plataforma &&
      this.estado &&

      this.juegoIntercambioSeleccionado &&
      this.plataformaIntercambio &&
      this.estadoIntercambio
    );
  }

  publicar() {

    const dto = {

      nombreProducto: this.nombreJuego,
      plataforma: this.plataforma,
      estadoProducto: this.estado,
      idApi: this.juegoSeleccionado.id,

      nombreProductoIntercambio:
        this.nombreJuegoIntercambio,

      plataformaIntercambio:
        this.plataformaIntercambio,

      estadoProductoIntercambio:
        this.estadoIntercambio,

      idApiIntercambio:
        this.juegoIntercambioSeleccionado.id,

      descripcion: this.descripcion
    };

    if (this.modoEdicion && this.postEditar?.id) {

      this.postService
        .actualizarIntercambio(this.postEditar.id, dto)
        .subscribe({
          next: () => {
            alert('Intercambio actualizado ✅');
            this.close.emit(true);
          },
          error: err => {
            console.error(err);
            alert('Error al actualizar ❌');
          }
        });

    } else {

      this.postService
        .crearIntercambio(dto)
        .subscribe({

          next: () => {
            alert('Intercambio publicado ✅');
            this.close.emit(true);
          },

          error: err => {
            console.error(err);
            alert('Error al publicar ❌');
          }
        });
    }
  }

  onBlurJuego() {
    setTimeout(() => this.focusedJuego = false, 150);
  }

  onBlurJuegoIntercambio() {
    setTimeout(() => this.focusedJuegoIntercambio = false, 150);
  }

  onBlurPlataforma() {
    setTimeout(() => this.focusedPlataforma = false, 150);
  }

  onBlurPlataformaIntercambio() {
    setTimeout(() => this.focusedPlataformaIntercambio = false, 150);
  }

  limpiarJuego() {
    this.nombreJuego = '';
    this.resultadosBusqueda = [];
    this.juegoSeleccionado = null;
  }

  limpiarJuegoIntercambio() {
    this.nombreJuegoIntercambio = '';
    this.resultadosBusquedaIntercambio = [];
    this.juegoIntercambioSeleccionado = null;
  }
}