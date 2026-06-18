import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class HomeService {
  private apiUrl = `${environment.apiUrl}/home`;

  constructor(private http: HttpClient) {}

  getUltimasVentas() {
    return this.http.get<any[]>(`${this.apiUrl}/ultimas-ventas`);
  }

  getUltimosIntercambios() {
    return this.http.get<any[]>(`${this.apiUrl}/ultimos-intercambios`);
  }

  getEstadisticas() {
    return this.http.get<any>(`${this.apiUrl}/estadisticas`);
  }

  getTopUsuarios() {
    return this.http.get<any[]>(`${this.apiUrl}/top-usuarios`);
  }
}
