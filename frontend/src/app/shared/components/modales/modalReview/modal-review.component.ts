import { Component, EventEmitter, Input, Output } from '@angular/core';

import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ModalComponent } from '../modal/modal.component';

import { ReviewService } from '../../../../core/services/review.service';

import { UsuarioService } from '../../../../core/services/usuario.service';
import { AlertService } from '../../../../core/services/alert.service';

@Component({
  selector: 'app-modal-review',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ModalComponent],
  templateUrl: './modal-review.component.html',
  styleUrls: ['./modal-review.component.css'],
})
export class ModalReviewComponent {
  @Input() isOpen = false;
  @Input() post: any;
  @Input() tipo = '';

  @Output() close = new EventEmitter<void>();

  contenido = '';
  estrellas = 5;
  usuarioActual: any;
  loading = false;

  constructor(
    private reviewService: ReviewService,
    private usuarioService: UsuarioService,
    private alertService: AlertService,
  ) {}

  ngOnInit() {
    this.usuarioService.getPerfil().subscribe((usuario) => {
      this.usuarioActual = usuario;
    });
  }

  estrellasArray = [0, 1, 2, 3, 4, 5];

  enviar() {
    if (!this.contenido.trim()) {
      this.alertService.info('Escribe un comentario para enviar tu review');
      return;
    }

    if (this.estrellas <= 0) {
      this.alertService.info('Selecciona estrellas para enviar tu review');
      return;
    }

    let idReviewed = 0;

    if (this.tipo === 'VENTA') {
      idReviewed = this.post.idVendedor;
    } else {
      idReviewed =
        this.post.idUsuarioPublicador === this.usuarioActual.id
          ? this.post.idUsuarioCambio
          : this.post.idUsuarioPublicador;
    }

    const dto = {
      idReviewed,
      tipoReview: this.tipo,
      idHistorial: this.post.id,
      contenido: this.contenido,
      estrellas: this.estrellas,
    };

    this.loading = true;

    this.reviewService.crearReview(dto).subscribe({
      next: () => {
        this.alertService.success('Review enviada');
        this.loading = false;
        this.close.emit();
      },

      error: (err) => {
        console.error(err);
        this.alertService.error('Error al enviar la review');
        this.loading = false;
      },
    });
  }

  getNombreUsuario(): string {
    if (this.tipo === 'VENTA') {
      return this.post.nombreVendedor;
    }

    return this.post.idUsuarioPublicador === this.usuarioActual.id
      ? this.post.nombreUsuarioCambio
      : this.post.nombreUsuarioPublicador;
  }

  usuarioOpen = true;
  contenidoOpen = true;
  puntuacionOpen = true;
}
