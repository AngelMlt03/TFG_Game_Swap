import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-modal-historial-detalle',
  standalone: true,
  imports: [ModalComponent, NgIf],
  templateUrl: './modal-historial-detalle.component.html',
  styleUrls: ['./modal-historial-detalle.component.css']
})

export class ModalHistorialDetalleComponent {

  @Input() isOpen = false;

  @Input()
  post: any;

  @Output()
  close = new EventEmitter<void>();
}