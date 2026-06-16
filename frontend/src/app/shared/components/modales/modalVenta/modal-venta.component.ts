import {
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  Output,
  EventEmitter,
} from '@angular/core';
import { PostService } from '../../../../core/services/post.service';
import { IgdbService } from '../../../../core/services/igdb.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';
import { AlertService } from '../../../../core/services/alert.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-modal-venta',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ModalComponent],
  templateUrl: './modal-venta.component.html',
  styleUrls: ['./modal-venta.component.css'],
})
export class ModalVentaComponent implements OnChanges {
  @Input() isOpen = false;
  @Input() modoEdicion = false;
  @Input() postEditar: any = null;

  @Output() close = new EventEmitter<boolean>();
  @Output() convertirAIntercambio = new EventEmitter<any>();

  estados = [
    { value: 'NUEVO', label: 'Nuevo' },
    { value: 'SEMINUEVO', label: 'Seminuevo' },
    { value: 'USADO', label: 'Usado' },
    { value: 'MUY_USADO', label: 'Muy usado' },
  ];

  modoConversion = false;
  idIntercambioOriginal?: number;

  // FORM
  idJuego = '';
  nombreJuego = '';
  plataforma = '';
  estado = '';
  precio: number | null = null;
  descripcion = '';

  juegoSeleccionado: any = null;
  imagen: string | null = null;

  resultadosBusqueda: any[] = [];

  idProductoEditar: number | null = null;

  focusedJuego = false;
  loadingJuego = false;

  focusedPlataforma = false;
  loadingPlataforma = false;

  focusedEstado = false;

  sugerenciasPlataforma: any[] = [];
  sugerenciasEstado = this.estados;

  constructor(
    private igdbService: IgdbService,
    private postService: PostService,
    private alertService: AlertService,
    private router: Router,
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['postEditar'] && this.postEditar) {
      this.nombreJuego = this.postEditar.nombreProducto;
      this.plataforma = this.postEditar.plataforma;
      this.estado = this.postEditar.estado;
      this.precio = this.postEditar.precio;
      this.descripcion = this.postEditar.descripcion || '';

      this.imagen = this.postEditar.imagen;

      this.idProductoEditar = this.postEditar.idProducto;

      // importante para que el formulario sea válido en edición
      this.juegoSeleccionado = {
        id: this.postEditar.idApi,
        nombre: this.postEditar.nombreProducto,
      };
      this.idJuego = this.postEditar.idApi;
    }
    if (this.postEditar?.modoConversion) {
      this.modoConversion = true;

      this.idIntercambioOriginal = this.postEditar.idIntercambioOriginal;
    }
  }

  // BUSCAR JUEGOS
  buscarJuego() {
    if (!this.nombreJuego || this.nombreJuego.length < 2) {
      this.resultadosBusqueda = [];
      return;
    }

    this.loadingJuego = true;

    this.igdbService.buscarJuegos(this.nombreJuego).subscribe({
      next: (res) => {
        this.resultadosBusqueda = res;
        this.loadingJuego = false;
      },
      error: () => {
        this.loadingJuego = false;
      },
    });
  }

  seleccionarJuego(juego: any) {
    this.juegoSeleccionado = juego;
    this.nombreJuego = juego.nombre;
    this.imagen = juego.imagen;
    this.resultadosBusqueda = [];
  }

  buscarPlataforma() {
    if (!this.plataforma || this.plataforma.length < 1) {
      this.sugerenciasPlataforma = [];
      return;
    }

    this.loadingPlataforma = true;

    this.igdbService.buscarPlataformas(this.plataforma).subscribe({
      next: (res) => {
        this.sugerenciasPlataforma = res;
        this.loadingPlataforma = false;
      },
      error: () => {
        this.loadingPlataforma = false;
      },
    });
  }

  seleccionarPlataforma(p: any) {
    this.plataforma = p.nombre;
    this.sugerenciasPlataforma = [];
    this.focusedPlataforma = false;
  }

  seleccionarEstado(estado: any) {
    this.estado = estado.value;
    this.focusedEstado = false;
  }

  // VALIDACIÓN
  formularioValido(): boolean {
    return !!(
      this.juegoSeleccionado &&
      this.plataforma &&
      this.estado &&
      this.precio
    );
  }

  // PUBLICAR
  publicar() {
    const dto = {
      idProducto: this.idProductoEditar,
      nombreProducto: this.nombreJuego,
      plataforma: this.plataforma,
      estadoProducto: this.estado,
      precio: Number(this.precio),
      descripcion: this.descripcion,
      idApi: this.juegoSeleccionado?.id,
    };

    if (this.modoEdicion && this.postEditar?.id) {
      this.postService.actualizarVenta(this.postEditar.id, dto).subscribe({
        next: () => {
          this.alertService.success('Publicación actualizada');
          this.close.emit(true);
        },
        error: (err) => {
          console.error(err);
          this.alertService.error('Error al actualizar la publicación');
        },
      });
    } else {
      this.postService.crearVenta(dto).subscribe({
        next: () => {
          this.alertService.success('Publicación creada');
          this.close.emit(true);
          this.router.navigate(['/perfil'], {
            queryParams: { tab: 'misPublicaciones' },
          });
        },
        error: (err) => {
          console.error(err);
          this.alertService.error('Error al publicar la publicación');
        },
      });
    }

    if (this.modoConversion) {
      this.postService
        .convertirIntercambioAVenta(this.idIntercambioOriginal!, dto)
        .subscribe({
          next: () => {
            this.alertService.success('Intercambio convertido a venta');

            this.close.emit(true);
          },
        });

      return;
    }
  }

  onFocusJuego() {
    this.focusedJuego = true;
  }

  onBlurJuego() {
    setTimeout(() => {
      this.focusedJuego = false;
    }, 150);
  }

  onFocusPlataforma() {
    this.focusedPlataforma = true;
  }

  onBlurPlataforma() {
    setTimeout(() => {
      this.focusedPlataforma = false;
    }, 150);
  }

  onBlurEstado() {
    setTimeout(() => {
      this.focusedEstado = false;
    }, 150);
  }

  getEstadoLabel(valor: string): string {
    return this.estados.find((e) => e.value === valor)?.label || '';
  }

  infoOpen = false;
  descripcionOpen = false;

  cambiarAIntercambio() {
    this.convertirAIntercambio.emit({
      idVenta: this.postEditar.id,

      idApi: this.idJuego,
      nombreProducto: this.nombreJuego,
      plataforma: this.plataforma,
      estado: this.estado,

      descripcion: this.descripcion,

      imagen: this.imagen,
    });

    this.close.emit(false);
  }
}
