import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
} from '@angular/core';

import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ModalComponent } from '../modal/modal.component';
import { IgdbService } from '../../../../core/services/igdb.service';
import { PostService } from '../../../../core/services/post.service';
import { AlertService } from '../../../../core/services/alert.service';
import { Router } from '@angular/router';
import { ModalDireccionComponent } from '../modalDireccion/modal-direccion.component';

@Component({
  selector: 'app-modal-intercambio',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ModalComponent, ModalDireccionComponent],
  templateUrl: './modal-intercambio.component.html',
  styleUrls: ['./modal-intercambio.component.css'],
})
export class ModalIntercambioComponent implements OnChanges {
  @Input() isOpen = false;
  @Input() modoEdicion = false;
  @Input() postEditar: any = null;

  @Output() close = new EventEmitter<boolean>();
  @Output() convertirAVenta = new EventEmitter<any>();

  mostrarModalDireccion = false;

  mostrarAvisoIntercambio = false;

  estados = [
    { value: 'NUEVO', label: 'Nuevo' },
    { value: 'SEMINUEVO', label: 'Seminuevo' },
    { value: 'USADO', label: 'Usado' },
    { value: 'MUY_USADO', label: 'Muy usado' },
  ];

  estadosIntercambio = [
    { value: 'CUALQUIERA', label: 'Cualquiera' },
    { value: 'NUEVO', label: 'Nuevo' },
    { value: 'SEMINUEVO', label: 'Seminuevo' },
    { value: 'USADO', label: 'Usado' },
    { value: 'MUY_USADO', label: 'Muy usado' },
  ];

  modoConversion = false;
  idVentaOriginal?: number;

  idJuego = '';
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
  focusedEstado = false;
  focusedEstadoIntercambio = false;

  loadingJuego = false;
  loadingJuegoIntercambio = false;
  loadingPlataforma = false;
  loadingPlataformaIntercambio = false;

  sugerenciasPlataforma: any[] = [];
  sugerenciasPlataformaIntercambio: any[] = [];
  sugerenciasEstado = this.estados;
  sugerenciasEstadoIntercambio = this.estadosIntercambio;

  constructor(
    private igdbService: IgdbService,
    private postService: PostService,
    private alertService: AlertService,
    private router: Router,
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
        nombre: this.postEditar.nombreProducto,
      };

      // JUEGO INTERCAMBIO
      this.nombreJuegoIntercambio = this.postEditar.nombreProductoIntercambio;
      this.plataformaIntercambio = this.postEditar.plataformaIntercambio;
      this.estadoIntercambio = this.postEditar.estadoIntercambio;
      this.imagenIntercambio = this.postEditar.imagenIntercambio;
      this.juegoIntercambioSeleccionado = {
        id: this.postEditar.idApiIntercambio,
        nombre: this.postEditar.nombreProductoIntercambio,
      };

      this.descripcion = this.postEditar.descripcion || '';
      this.idJuego = this.postEditar.idApi;
    }
    if (this.postEditar?.modoConversion) {
      this.modoConversion = true;

      this.idVentaOriginal = this.postEditar.idVentaOriginal;
    }
  }

  buscarJuego() {
    if (this.nombreJuego.length < 3) return;

    this.igdbService.buscarJuegos(this.nombreJuego).subscribe((res) => {
      this.resultadosBusqueda = res;
    });
  }

  seleccionarJuego(juego: any) {
    this.juegoSeleccionado = juego;

    this.nombreJuego = juego.nombre;
    this.imagen = juego.imagen;

    this.resultadosBusqueda = [];

    this.focusedJuego = false;
    this.comprobarIntercambioExistente();
  }

  buscarPlataforma() {
    if (!this.plataforma) {
      this.sugerenciasPlataforma = [];
      return;
    }

    this.loadingPlataforma = true;

    this.igdbService.buscarPlataformas(this.plataforma).subscribe({
      next: (res) => {
        this.sugerenciasPlataforma = res;
        this.loadingPlataforma = false;
      },
      error: () => (this.loadingPlataforma = false),
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
      .subscribe((res) => {
        this.resultadosBusquedaIntercambio = res;
      });
  }

  seleccionarJuegoIntercambio(juego: any) {
    this.juegoIntercambioSeleccionado = juego;

    this.nombreJuegoIntercambio = juego.nombre;
    this.imagenIntercambio = juego.imagen;

    this.resultadosBusquedaIntercambio = [];

    this.focusedJuegoIntercambio = false;
    this.comprobarIntercambioExistente(); 
  }

  buscarPlataformaIntercambio() {
    if (this.plataformaIntercambio.length < 2) return;

    if (!this.plataformaIntercambio) {
      this.sugerenciasPlataformaIntercambio = [];
      return;
    }

    this.loadingPlataformaIntercambio = true;
    this.igdbService.buscarPlataformas(this.plataformaIntercambio).subscribe({
      next: (res) => {
        this.sugerenciasPlataformaIntercambio = res;
        this.loadingPlataformaIntercambio = false;
      },
      error: () => (this.loadingPlataformaIntercambio = false),
    });
  }

  seleccionarPlataformaIntercambio(p: any) {
    this.plataformaIntercambio = p.nombre;
    this.focusedPlataformaIntercambio = false;
    this.sugerenciasPlataformaIntercambio = [];
  }

  seleccionarEstado(estado: any) {
    this.estado = estado.value;
    this.focusedEstado = false;
  }

  seleccionarEstadoIntercambio(estado: any) {
    this.estadoIntercambio = estado.value;
    this.focusedEstadoIntercambio = false;
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
    this.mostrarModalDireccion = true;
  }

  confirmarPublicar() {
    this.mostrarModalDireccion = false;

    const dto = {
      nombreProducto: this.nombreJuego,
      plataforma: this.plataforma,
      estadoProducto: this.estado,
      idApi: this.juegoSeleccionado.id,

      nombreProductoIntercambio: this.nombreJuegoIntercambio,
      plataformaIntercambio: this.plataformaIntercambio,
      estadoProductoIntercambio: this.estadoIntercambio,
      idApiIntercambio: this.juegoIntercambioSeleccionado.id,

      descripcion: this.descripcion,
    };

    if (this.modoEdicion && this.postEditar?.id) {
      this.postService
        .actualizarIntercambio(this.postEditar.id, dto)
        .subscribe({
          next: () => {
            this.alertService.success('Intercambio actualizado');
            this.close.emit(true);
          },
          error: (err) => {
            console.error(err);
            this.alertService.error('Error al actualizar el intercambio');
          },
        });
    } else {
      this.postService.crearIntercambio(dto).subscribe({
        next: () => {
          this.alertService.success('Intercambio publicado');
          this.close.emit(true);
          this.router.navigate(['/perfil'], {
            queryParams: { tab: 'misPublicaciones' },
          });
        },

        error: (err) => {
          console.error(err);
          this.alertService.error('Error al publicar el intercambio');
        },
      });
    }

    if (this.modoConversion) {
      this.postService
        .convertirVentaAIntercambio(this.idVentaOriginal!, dto)
        .subscribe({
          next: () => {
            this.alertService.success('Venta convertida a intercambio');

            this.close.emit(true);
          },
        });

      return;
    }
  }

  cancelarPublicar() {
    this.mostrarModalDireccion = false;
  }

  onBlurJuego() {
    setTimeout(() => (this.focusedJuego = false), 150);
  }

  onBlurJuegoIntercambio() {
    setTimeout(() => (this.focusedJuegoIntercambio = false), 150);
  }

  onBlurPlataforma() {
    setTimeout(() => (this.focusedPlataforma = false), 150);
  }

  onBlurPlataformaIntercambio() {
    setTimeout(() => (this.focusedPlataformaIntercambio = false), 150);
  }

  onBlurEstado() {
    setTimeout(() => {
      this.focusedEstado = false;
    }, 150);
  }

  onBlurEstadoIntercambio() {
    setTimeout(() => {
      this.focusedEstadoIntercambio = false;
    }, 150);
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

  getEstadoLabel(valor: string): string {
    return this.estados.find((e) => e.value === valor)?.label || '';
  }

  getEstadoIntercambioLabel(valor: string): string {
    return this.estadosIntercambio.find((e) => e.value === valor)?.label || '';
  }

  mostrarTuJuego = false;
  mostrarJuegoBuscado = false;
  mostrarDescripcion = false;

  tuVideojuegoOpen = true;
  juegoBuscadoOpen = true;
  DescripcionOpen = true;

  cambiarAVenta() {
    this.convertirAVenta.emit({
      idIntercambio: this.postEditar.id,

      idApi: this.idJuego,
      nombreProducto: this.nombreJuego,
      plataforma: this.plataforma,
      estado: this.estado,

      descripcion: this.descripcion,

      imagen: this.imagen,
    });

    this.close.emit(false);
  }

  comprobarIntercambioExistente() {
    if (!this.nombreJuego?.trim() || !this.nombreJuegoIntercambio?.trim()) {
      this.mostrarAvisoIntercambio = false;
      return;
    }

    this.postService
      .existeIntercambioSugerido(this.nombreJuego, this.nombreJuegoIntercambio)
      .subscribe((existe) => {
        this.mostrarAvisoIntercambio = existe;
      });
  }
}
