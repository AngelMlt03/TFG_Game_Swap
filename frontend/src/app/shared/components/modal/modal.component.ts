import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-modal',
  imports: [NgIf],
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent {

  @Input() isOpen = false;
  @Input() title = '';

  @Output() close = new EventEmitter<void>();

  cerrar() {
    this.close.emit();
  }
}