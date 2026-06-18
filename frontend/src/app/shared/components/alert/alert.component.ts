import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css'],
})
export class AlertComponent implements OnInit {
  visible = false;

  mensaje = '';

  tipo: 'success' | 'error' | 'info' = 'info';

  timeout: any;

  constructor(private alertService: AlertService) {}

  ngOnInit() {
    this.alertService.alert$.subscribe((alert) => {
      this.mensaje = alert.mensaje;

      this.tipo = alert.tipo;

      this.visible = true;

      clearTimeout(this.timeout);

      this.timeout = setTimeout(() => {
        this.cerrar();
      }, 3500);
    });
  }

  cerrar() {
    this.visible = false;
  }
}
