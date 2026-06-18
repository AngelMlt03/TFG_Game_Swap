import { Component, Input, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';

import { ReviewService } from '../../../core/services/review.service';
import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';

import { ModalHistorialDetalleComponent } from '../modales/modalHistorial/modal-historial-detalle.component';
import { AlertService } from '../../../core/services/alert.service';
import { ConfirmDialogService } from '../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [NgFor, NgIf, ModalHistorialDetalleComponent],
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css'],
})
export class ReviewsComponent implements OnInit {

  @Input() nombreUsuario?: string;
  @Input() esPerfilPublico: boolean = false;

  reviewsRecibidas: any[] = [];
  reviewsEnviadas: any[] = [];

  modalDetalleOpen = false;
  postDetalle: any = null;

  imagenes: { [key: number]: string } = {};

  constructor(
    private reviewService: ReviewService,
    private postService: PostService,
    private igdbService: IgdbService,
    private alertService: AlertService,
    private confirmDialogService: ConfirmDialogService,
  ) {}

  ngOnInit() {
    this.cargarReviewsRecibidas();
    this.cargarReviewsEnviadas();
  }

  cargarReviewsRecibidas() {
    this.reviewService.getMisReviews(this.nombreUsuario!).subscribe((res) => {
      this.reviewsRecibidas = res;
    });
  }

  cargarReviewsEnviadas() {
    this.reviewService.getReviewsEnviadas().subscribe((res) => {
      this.reviewsEnviadas = res;
    });
  }

  avatar(nombre: string) {
    return `https://ui-avatars.com/api/?name=${nombre}&background=random`;
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

  async eliminarReview(id: number) {
    const confirmado = await this.confirmDialogService.confirmar(
      '¿Eliminar review?',
    );

    if (!confirmado) return;

    this.reviewService.eliminarReview(id).subscribe({
      next: () => {
        this.reviewsEnviadas = this.reviewsEnviadas.filter((r) => r.id !== id);
        this.alertService.success('Review eliminada correctamente');
      },

      error: (err) => {
        console.error(err);
        this.alertService.error('Error al eliminar review');
      },
    });
  }

  verDetalles(review: any) {
    // REVIEW DE VENTA

    if (review.tipoReview === 'VENTA') {
      this.postService.getCompraById(review.idCompraVenta).subscribe((res) => {
        res.tipo = 'VENTA';

        this.cargarImagen(res.idApiProducto);

        res.imagen = this.getImagen(res.idApiProducto);

        this.postDetalle = res;

        this.modalDetalleOpen = true;
      });
    } else {
      // REVIEW DE INTERCAMBIO

      this.postService
        .getIntercambioById(review.idIntercambio)
        .subscribe((res) => {
          res.tipo = 'INTERCAMBIO';

          this.cargarImagen(res.idApiProductoOfrecido);

          this.cargarImagen(res.idApiProductoDeseado);

          res.imagen = this.getImagen(res.idApiProductoOfrecido);

          res.imagenIntercambio = this.getImagen(res.idApiProductoDeseado);

          this.postDetalle = res;

          this.modalDetalleOpen = true;
        });
    }
  }

  cerrarDetalle() {
    this.modalDetalleOpen = false;

    this.postDetalle = null;
  }

  enviadasOpen = true;
  recibidasOpen = true;
}
