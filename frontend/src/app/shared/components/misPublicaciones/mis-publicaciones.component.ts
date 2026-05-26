import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { PostService } from '../../../core/services//post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { ModalVentaComponent } from '../modales/modalVenta/modal-venta.component';
import { ModalIntercambioComponent } from '../modales/modalIntercambio/modal-intercambio.component';
import { AlertService } from '../../../core/services/alert.service';
import { ConfirmDialogService } from '../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-mis-publicaciones',
  standalone: true,
  imports: [NgFor, NgIf, ModalVentaComponent, ModalIntercambioComponent],
  templateUrl: './mis-publicaciones.component.html',
  styleUrls: ['./mis-publicaciones.component.css'],
})
export class MisPublicacionesComponent implements OnInit {
  ventas: any[] = [];
  intercambios: any[] = [];

  imagenes: { [key: number]: string } = {};

  modalEditarOpen = false;
  postEditar: any = null;

  modalEditarIntercambioOpen = false;
  postEditarIntercambio: any = null;

  constructor(
    private postService: PostService,
    private igdbService: IgdbService,
    private alertService: AlertService,
    private confirmDialogService: ConfirmDialogService,
  ) {}

  ngOnInit() {
    this.cargarVentas();
    this.cargarIntercambios();
  }

  cargarVentas() {
    this.postService.getMisVentas().subscribe((res) => {
      this.ventas = res;
      res.forEach((v) => this.cargarImagen(v.idApi));
    });
  }

  cargarIntercambios() {
    this.postService.getMisIntercambios().subscribe((res) => {
      this.intercambios = res;

      res.forEach((i) => {
        this.cargarImagen(i.idApi);
        this.cargarImagen(i.idApiIntercambio);
      });
    });
  }

  cargarImagen(idApi: number) {
    if (this.imagenes[idApi]) return;

    this.igdbService.getCover(idApi).subscribe((url) => {
      this.imagenes[idApi] = url || 'assets/no-image.png';
    });
  }

  getImagen(idApi: number) {
    return this.imagenes[idApi] || 'assets/no-image.png';
  }

  editarVenta(post: any) {
    post.imagen = this.getImagen(post.idApi);
    this.postEditar = post;
    this.modalEditarOpen = true;
  }

  async eliminarVenta(id: number) {
    const confirmado =
      await this.confirmDialogService.confirmar('¿Eliminar venta?');

    if (!confirmado) return;

    this.postService.eliminarVenta(id).subscribe({
      next: () => {
        this.ventas = this.ventas.filter((p) => p.id !== id);

        this.alertService.success('Compra realizada');
      },
      error: (err) => {
        console.error(err);
        this.alertService.error('Error al eliminar');
      },
    });
  }

  editarIntercambio(post: any) {
    post.imagen = this.getImagen(post.idApi);
    post.imagenIntercambio = this.getImagen(post.idApiIntercambio);

    this.postEditarIntercambio = post;
    this.modalEditarIntercambioOpen = true;
  }

  async eliminarIntercambio(id: number) {
    const confirmado = await this.confirmDialogService.confirmar(
      '¿Eliminar intercambio?',
    );

    if (!confirmado) return;

    this.postService.eliminarIntercambio(id).subscribe({
      next: () => {
        this.intercambios = this.intercambios.filter((i) => i.id !== id);
        this.alertService.success('Intercambio eliminado');
      },
      error: (err) => {
        console.error(err);
        this.alertService.error('Error al eliminar');
      },
    });
  }

  cerrarModalEditar(actualizar: boolean) {
    this.modalEditarOpen = false;
    this.postEditar = null;

    if (actualizar) {
      this.cargarVentas();
      this.cargarIntercambios();
    }
  }

  cerrarModalEditarIntercambio(actualizar: boolean) {
    this.modalEditarIntercambioOpen = false;
    this.postEditarIntercambio = null;

    if (actualizar) {
      this.cargarIntercambios();
    }
  }

  intercambiosOpen = true;
  ventasOpen = true;
}
