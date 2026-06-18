import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  private alertSubject = new Subject<any>();

  alert$ = this.alertSubject.asObservable();

  success(mensaje: string) {
    this.alertSubject.next({
      mensaje,
      tipo: 'success',
    });
  }

  error(mensaje: string) {
    this.alertSubject.next({
      mensaje,
      tipo: 'error',
    });
  }

  info(mensaje: string) {
    this.alertSubject.next({
      mensaje,
      tipo: 'info',
    });
  }
}
