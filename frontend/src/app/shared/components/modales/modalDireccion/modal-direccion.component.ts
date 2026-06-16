import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';

@Component({
  selector: 'app-modal-direccion',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent],
  templateUrl: './modal-direccion.component.html',
  styleUrls: ['./modal-direccion.component.css'],
})
export class ModalDireccionComponent {
  @Output() confirmar = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  direccion = '';
  ciudad = '';
  provincia = '';
  codigoPostal = '';

  submitted = false;

  formularioValido(): boolean {
    return (
      this.direccion.trim() !== '' &&
      this.ciudad.trim() !== '' &&
      this.provincia.trim() !== '' &&
      this.codigoPostal.trim() !== ''
    );
  }

  onConfirmar() {
    this.submitted = true;

    if (!this.formularioValido()) {
      return;
    }

    this.confirmar.emit();
  }

  onCancelar() {
    this.cancelar.emit();
  }
}
