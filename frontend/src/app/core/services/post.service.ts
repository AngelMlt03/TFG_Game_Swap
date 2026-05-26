import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/internal/operators/map';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  buscarPosts(filtros: any) {
    let params = new HttpParams();

    if (filtros.videojuego) {
      params = params.set('nombre', filtros.videojuego);
    }

    if (filtros.tipo) {
      params = params.set('tipo', filtros.tipo);
    }

    if (filtros.estado) {
      params = params.set('estado', filtros.estado);
    }

    if (filtros.plataforma) {
      params = params.set('plataforma', filtros.plataforma);
    }

    return this.http.get<any[]>(`${this.apiUrl}/busqueda`, { params });
  }

  comprar(postId: number) {
    return this.http
      .post(`${this.apiUrl}/transaccion/comprar/${postId}`, {})
      .pipe(map((response: any) => response.saldo));
  }

  intercambiar(postId: number) {
    return this.http.post(
      `${this.apiUrl}/transaccion/intercambiar/${postId}`,
      {},
    );
  }

  crearVenta(dto: any) {
    return this.http.post(`${this.apiUrl}/posts-venta`, dto);
  }

  crearIntercambio(dto: any) {
    return this.http.post(`${this.apiUrl}/posts-intercambio`, dto);
  }

  getMisVentas() {
    return this.http.get<any[]>(`${this.apiUrl}/busqueda/mis-ventas`);
  }

  getMisIntercambios() {
    return this.http.get<any[]>(`${this.apiUrl}/busqueda/mis-intercambios`);
  }

  actualizarVenta(id: number, dto: any) {
    return this.http.put(`${this.apiUrl}/posts-venta/${id}`, dto);
  }

  eliminarVenta(id: number) {
    return this.http.delete(`${this.apiUrl}/posts-venta/${id}`);
  }

  actualizarIntercambio(id: number, dto: any) {
    return this.http.put(`${this.apiUrl}/posts-intercambio/${id}`, dto);
  }

  eliminarIntercambio(id: number) {
    return this.http.delete(`${this.apiUrl}/posts-intercambio/${id}`);
  }

  getHistorialCompras() {
    return this.http.get<any[]>(`${this.apiUrl}/historial/compras`);
  }

  getHistorialVentas() {
    return this.http.get<any[]>(`${this.apiUrl}/historial/ventas`);
  }

  getHistorialIntercambios() {
    return this.http.get<any[]>(`${this.apiUrl}/historial/intercambios`);
  }

  guardarPost(idPost: number, tipoPost: string) {
    return this.http.post(`${this.apiUrl}/guardados`, {
      idPost,
      tipoPost,
    });
  }

  estaGuardado(idPost: number, tipoPost: string) {
    return this.http.get<boolean>(
      `${this.apiUrl}/guardados/existe/${idPost}/${tipoPost}`,
    );
  }

  eliminarGuardado(idPost: number, tipoPost: string) {
    return this.http.delete(`${this.apiUrl}/guardados/${idPost}/${tipoPost}`);
  }

  getVentasGuardadas() {
    return this.http.get<any[]>(`${this.apiUrl}/guardados/ventas`);
  }

  getIntercambiosGuardados() {
    return this.http.get<any[]>(`${this.apiUrl}/guardados/intercambios`);
  }

  getCompraById(id: number) {
    return this.http.get<any>(`${this.apiUrl}/reviews/compra/${id}`);
  }

  getIntercambioById(id: number) {
    return this.http.get<any>(`${this.apiUrl}/reviews/intercambio/${id}`);
  }

  agregarCarrito(idVenta: number) {
    return this.http.post(`${this.apiUrl}/carrito/${idVenta}`, {});
  }

  eliminarCarrito(idVenta: number) {
    return this.http.delete(`${this.apiUrl}/carrito/${idVenta}`);
  }

  estaEnCarrito(idVenta: number) {
    return this.http.get<boolean>(`${this.apiUrl}/carrito/exists/${idVenta}`);
  }

  obtenerCarrito() {
    return this.http.get<any[]>(`${this.apiUrl}/carrito`);
  }

  vaciarCarrito() {
    return this.http.delete(`${this.apiUrl}/carrito/vaciar`);
  }

  obtenerPrecioCarrito() {
    return this.http.get<number>(`${this.apiUrl}/carrito/precio`);
  }
}
