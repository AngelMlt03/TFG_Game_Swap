import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../modal/modal.component';
import { PostService } from '../../../../core/services/post.service';
import { UsuarioService } from '../../../../core/services/usuario.service';
import { Router } from '@angular/router';
import { AlertService } from '../../../../core/services/alert.service';
import { IgdbService } from '../../../../core/services/igdb.service';
import { ModalDireccionComponent } from '../modalDireccion/modal-direccion.component';

@Component({
  selector: 'app-post-detalle',
  standalone: true,
  imports: [CommonModule, ModalComponent, ModalDireccionComponent],
  templateUrl: './post-detalle.component.html',
  styleUrls: ['./post-detalle.component.css'],
})
export class PostDetalleComponent implements OnInit, OnChanges {
  @Input() isOpen = false;
  @Input() post: any;
  @Input() externPostImg: any;
  @Input() externPostIntercambioImg: any;

  @Output() close = new EventEmitter<void>();
  @Output() accionRealizada = new EventEmitter<void>();

  loading = false;
  saldo = 0;
  guardado = false;
  enCarrito = false;

  gameDetails: any;
  imagenExpandida: string | null = null;

  mostrarModalDireccion = false;
  tipoAccionDireccion: 'COMPRA' | 'INTERCAMBIO' = 'COMPRA';

  constructor(
    private postService: PostService,
    private usuarioService: UsuarioService,
    private router: Router,
    private alertService: AlertService,
    private igdbService: IgdbService,
  ) {}

  ngOnInit() {
    this.infoOpen = false;
    this.descripcionOpen = false;
    this.usuarioService.saldo$.subscribe((s) => (this.saldo = s));

    if (this.usuarioService.getSaldo() === 0) {
      this.usuarioService.getSaldoFromBackend().subscribe((s) => {
        this.usuarioService.setSaldo(s);
      });
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    this.infoOpen = false;
    this.descripcionOpen = false;
    if (changes['post'] && this.post?.id && this.post?.tipo) {
      this.postService
        .estaGuardado(this.post.id, this.post.tipo)
        .subscribe((res) => {
          this.guardado = res;
        });
    }
    if (this.post?.tipo === 'VENTA') {
      this.postService.estaEnCarrito(this.post.id).subscribe((res) => {
        this.enCarrito = res;
      });
    }
    if (this.post?.idApi) {
      this.igdbService.getGameDetails(this.post.idApi).subscribe((res) => {
        this.gameDetails = res[0];
      });
    }

    if (this.externPostImg) {
      this.post = { ...this.post, imagen: this.externPostImg };
    }
    if (this.externPostIntercambioImg) {
      this.post = { ...this.post, imagenIntercambio: this.externPostIntercambioImg };
    }
  }

  cerrar() {
    this.close.emit();
  }

  tieneSaldo(): boolean {
    return this.saldo >= (this.post?.precio || 0);
  }

  irASaldo() {
    this.cerrar();
    this.router.navigate(['/saldo']);
  }

  comprar() {
    this.tipoAccionDireccion = 'COMPRA';
    this.mostrarModalDireccion = true;
  }

  confirmarCompra() {
    if (!this.post?.id) return;

    this.loading = true;
    this.mostrarModalDireccion = false;

    this.postService.comprar(this.post.id).subscribe({
      next: (nuevoSaldo) => {
        this.usuarioService.setSaldo(nuevoSaldo);

        this.alertService.success('Compra realizada con éxito');

        this.loading = false;
        this.accionRealizada.emit();
        this.cerrar();

        this.router.navigate(['/perfil'], {
          queryParams: {
            tab: 'historial',
          },
        });
      },
      error: (err) => {
        console.error(err);

        this.alertService.error('Error al comprar');

        this.loading = false;
      },
    });
  }

  cancelarCompra() {
    this.mostrarModalDireccion = false;
  }

  intercambiar() {
    this.tipoAccionDireccion = 'INTERCAMBIO';
    this.mostrarModalDireccion = true;
  }

  confirmarIntercambio() {
    if (!this.post?.id) return;

    this.loading = true;
    this.mostrarModalDireccion = false;

    this.postService.intercambiar(this.post.id).subscribe({
      next: () => {
        this.alertService.success('Intercambio aceptado');

        this.loading = false;
        this.accionRealizada.emit();
        this.cerrar();

        this.router.navigate(['/perfil'], {
          queryParams: {
            tab: 'historial',
          },
        });
      },
      error: (err) => {
        console.error(err);

        this.alertService.error('Error en intercambio');

        this.loading = false;
      },
    });
  }

  cancelarIntercambio() {
    this.mostrarModalDireccion = false;
  }

  guardarPost() {
    if (!this.post?.id || !this.post?.tipo) return;

    if (this.guardado) {
      this.postService
        .eliminarGuardado(this.post.id, this.post.tipo)
        .subscribe({
          next: () => {
            this.guardado = false;
            this.accionRealizada.emit();
            this.alertService.success('Eliminado de guardados');
          },
          error: (err) => {
            console.error(err);
            this.alertService.error('Error al eliminar de guardados');
          },
        });
    } else {
      this.postService.guardarPost(this.post.id, this.post.tipo).subscribe({
        next: () => {
          this.guardado = true;
          this.accionRealizada.emit();
          this.alertService.success('Añadido a guardados');
        },
        error: (err) => {
          console.error(err);
          this.alertService.error('Error al guardar el post');
        },
      });
    }
  }

  infoOpen = false;
  descripcionOpen = false;

  toggleCarrito() {
    if (!this.post?.id) return;

    if (this.enCarrito) {
      this.postService.eliminarCarrito(this.post.id).subscribe(() => {
        this.enCarrito = false;

        this.postService.notificarActualizacionCarrito();

        this.alertService.success('Eliminado del carrito');
      });
    } else {
      this.postService.agregarCarrito(this.post.id).subscribe(() => {
        this.enCarrito = true;

        this.postService.notificarActualizacionCarrito();

        this.alertService.success('Añadido al carrito');
      });
    }
  }

  abrirImagen(url: string) {
    this.imagenExpandida = url;
  }

  cerrarImagen() {
    this.imagenExpandida = null;
  }
}
