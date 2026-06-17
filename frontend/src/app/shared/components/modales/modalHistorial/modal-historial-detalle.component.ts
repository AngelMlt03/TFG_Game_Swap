import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { TokenService } from '../../../../core/services/token.service';

@Component({
  selector: 'app-modal-historial-detalle',
  standalone: true,
  imports: [ModalComponent, NgIf],
  templateUrl: './modal-historial-detalle.component.html',
  styleUrls: ['./modal-historial-detalle.component.css'],
})
export class ModalHistorialDetalleComponent {
  @Input() isOpen = false;
  @Input() post: any;

  @Output() close = new EventEmitter<void>();

  infoOpen = false;
  descripcionOpen = false;

  constructor(
    private router: Router,
    private tokenService: TokenService,
  ) {}

  irAPerfil(nombreUsuario: string) {
    const miUsuario = this.tokenService.getUsername();

    if (
      miUsuario?.toLowerCase() === nombreUsuario.toLowerCase()
    ) {
      this.router.navigate(['/perfil']);
    } else {
      this.router.navigate(['/usuario', nombreUsuario]);
    }

    this.close.emit();
  }
}
