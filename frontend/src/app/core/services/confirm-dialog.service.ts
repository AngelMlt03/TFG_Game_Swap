import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConfirmDialogService {
  private confirmSubject = new Subject<any>();

  confirm$ = this.confirmSubject.asObservable();

  confirmar(mensaje: string): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmSubject.next({
        mensaje,
        resolve,
      });
    });
  }
}
