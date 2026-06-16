import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';

import { NgFor, NgIf } from '@angular/common';

import { Router } from '@angular/router';

import { ModalComponent } from '../modales/modal/modal.component';

import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-carrito-modal',
  standalone: true,
  imports: [NgFor, NgIf, ModalComponent],
  templateUrl: './carrito-modal.component.html',
  styleUrls: ['./carrito-modal.component.css'],
})
export class ModalCarritoComponent implements OnInit {
  @Input()
  isOpen = false;

  @Output()
  close = new EventEmitter<void>();

  productos: any[] = [];

  precioTotal = 0;

  saldo = 0;

  imagenes: { [key: number]: string } = {};

  loading = false;

  constructor(
    private postService: PostService,
    private igdbService: IgdbService,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.cargarCarrito();

    this.actualizarPrecioTotal();

    this.usuarioService.saldo$.subscribe((s) => (this.saldo = s));

    if (this.usuarioService.getSaldo() === 0) {
      this.usuarioService.getSaldoFromBackend().subscribe((s) => {
        this.usuarioService.setSaldo(s);
      });
    }
    
    this.postService.carritoActualizado$.subscribe(() => {
      this.cargarCarrito();
      this.actualizarPrecioTotal();
    });
  }

  cargarCarrito() {
    this.postService.obtenerCarrito().subscribe((res) => {
      this.productos = res;

      res.forEach((p: any) => {
        this.cargarImagen(p.idApi);
      });
    });
  }

  actualizarPrecioTotal() {
    this.postService.obtenerPrecioCarrito().subscribe((precio) => {
      this.precioTotal = precio;
    });
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

  tieneSaldo(precio: number): boolean {
    return this.saldo >= precio;
  }

  tieneSaldoTotal(): boolean {
    return this.saldo >= this.precioTotal;
  }

  irASaldo() {
    this.close.emit();

    this.router.navigate(['/saldo']);
  }

  comprar(post: any) {
    if (!this.tieneSaldo(post.precio)) {
      this.irASaldo();
      return;
    }

    this.loading = true;

    this.postService.comprar(post.idPostVenta).subscribe({
      next: (nuevoSaldo) => {
        this.usuarioService.setSaldo(nuevoSaldo);

        this.productos = this.productos.filter(
          (p) => p.idPostVenta !== post.idPostVenta,
        );

        this.alertService.success('Compra realizada');

        this.loading = false;

        this.actualizarPrecioTotal();
      },

      error: (err) => {
        console.error(err);

        this.alertService.error('Error al comprar');

        this.loading = false;
      },
    });
  }

  eliminar(post: any) {
    this.postService.eliminarCarrito(post.idPostVenta).subscribe({
      next: () => {
        this.productos = this.productos.filter(
          (p) => p.idPostVenta !== post.idPostVenta,
        );

        this.actualizarPrecioTotal();

        this.alertService.success('Producto eliminado del carrito');
      },

      error: (err) => {
        console.error(err);

        this.alertService.error('Error al eliminar del carrito');
      },
    });
  }

  comprarTodo() {
    if (!this.productos.length) return;

    if (!this.tieneSaldoTotal()) {
      this.irASaldo();
      return;
    }

    this.loading = true;

    const productos = [...this.productos];

    const comprarSiguiente = (index: number) => {
      if (index >= productos.length) {
        this.productos = [];

        this.alertService.success('Todo comprado correctamente');

        this.loading = false;

        this.actualizarPrecioTotal();

        this.close.emit();

        return;
      }

      this.postService.comprar(productos[index].idPostVenta).subscribe({
        next: (nuevoSaldo) => {
          this.usuarioService.setSaldo(nuevoSaldo);

          comprarSiguiente(index + 1);
        },

        error: (err) => {
          console.error(err);

          this.alertService.error('Error en una compra del carrito');

          this.loading = false;
        },
      });
    };

    comprarSiguiente(0);
  }

  vaciarCarrito() {
    this.postService.vaciarCarrito().subscribe({
      next: () => {
        this.productos = [];

        this.actualizarPrecioTotal();

        this.alertService.success('Carrito eliminado');

        this.close.emit();
      },

      error: (err) => {
        console.error(err);

        this.alertService.error('Error al vaciar el carrito');
      },
    });
  }
}
