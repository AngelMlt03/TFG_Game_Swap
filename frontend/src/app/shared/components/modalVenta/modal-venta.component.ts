import { Component, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';

@Component({
  selector: 'app-modal-venta',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ModalComponent],
  templateUrl: './modal-venta.component.html',
  styleUrls: ['./modal-venta-intercambio.component.css']
})
export class ModalVentaComponent implements OnChanges {

  @Input() isOpen = false;
  @Output() close = new EventEmitter<boolean>();
  @Input() modoEdicion = false;
  @Input() postEditar: any = null;

  // FORM
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

  sugerenciasPlataforma: any[] = [];

  constructor(
    private igdbService: IgdbService,
    private postService: PostService
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
        nombre: this.postEditar.nombreProducto
      };
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
      }
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
      }
    });
  }

  seleccionarPlataforma(p: any) {
    this.plataforma = p.nombre;
    this.sugerenciasPlataforma = [];
    this.focusedPlataforma = false;
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
      idApi: this.juegoSeleccionado?.id
    };

    if (this.modoEdicion && this.postEditar?.id) {

      this.postService
        .actualizarVenta(this.postEditar.id, dto)
        .subscribe({
          next: () => {
            alert('Publicación actualizada ✅');
            this.close.emit(true);
          },
          error: err => {
            console.error(err);
            alert('Error al actualizar ❌');
          }
        });

    } else {

      this.postService
        .crearVenta(dto)
        .subscribe({
          next: () => {
            alert('Publicación creada ✅');
            this.close.emit(true);
          },
          error: err => {
            console.error(err);
            alert('Error al publicar ❌');
          }
        });

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
}