import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { ConfirmDialogService } from '../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css'],
})
export class ConfirmDialogComponent implements OnInit {
  visible = false;

  mensaje = '';

  private resolveFn: ((value: boolean) => void) | null = null;

  constructor(private confirmService: ConfirmDialogService) {}

  ngOnInit() {
    this.confirmService.confirm$.subscribe((data) => {
      this.mensaje = data.mensaje;

      this.visible = true;

      this.resolveFn = data.resolve;
    });
  }

  confirmar() {
    this.visible = false;

    this.resolveFn?.(true);
  }

  cancelar() {
    this.visible = false;

    this.resolveFn?.(false);
  }
}
