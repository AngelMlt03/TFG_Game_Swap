import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private apiUrl = `${environment.apiUrl}/reviews`;

  constructor(
    private http: HttpClient
  ) {}

  getMisReviews(nombreUsuario: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reviews/${nombreUsuario}`);
  }

  getReviewsEnviadas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/enviadas`);
  }

  crearReview(dto: any): Observable<any> {
    return this.http.post(this.apiUrl, dto);
  }

  eliminarReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}