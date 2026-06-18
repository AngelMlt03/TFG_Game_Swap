import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class IgdbService {
  private apiUrl = `${environment.apiUrl}/igdb`;

  constructor(private http: HttpClient) {}

  buscarJuegos(nombre: string, franchiseId?: number) {
    let url = `${this.apiUrl}/games?query=${nombre}`;

    if (franchiseId) {
      url += `&franchiseId=${franchiseId}`;
    }

    return this.http.get<any[]>(url).pipe(
      map((res) =>
        res.map((j) => ({
          id: j.id,
          nombre: j.name,
          imagen: j.cover
            ? `https://images.igdb.com/igdb/image/upload/t_thumb/${j.cover.image_id}.jpg`
            : null,
        })),
      ),
    );
  }

  buscarFranquicias(nombre: string) {
    return this.http
      .get<any[]>(`${this.apiUrl}/franchises?query=${nombre}`)
      .pipe(
        map((res) =>
          res.map((f) => ({
            nombre: f.name,
            imagen: null,
          })),
        ),
      );
  }

  buscarPlataformas(nombre: string) {
    return this.http
      .get<any[]>(`${this.apiUrl}/platforms?query=${nombre}`)
      .pipe(
        map((res) =>
          res.map((p) => ({
            nombre: p.name,
            imagen: p.platform_logo
              ? `https://images.igdb.com/igdb/image/upload/t_thumb/${p.platform_logo.image_id}.jpg`
              : null,
          })),
        ),
      );
  }

  getCover(id: number) {
    return this.http.get<any[]>(`${this.apiUrl}/cover?id=${id}`).pipe(
      map((res) => {
        const imageId = res[0]?.cover?.image_id;
        return imageId
          ? `https://images.igdb.com/igdb/image/upload/t_cover_big/${imageId}.jpg`
          : null;
      }),
    );
  }

  getGameDetails(id: number) {
    return this.http.get<any[]>(`${this.apiUrl}/game-details?id=${id}`);
  }
}
