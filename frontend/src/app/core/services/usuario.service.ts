import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  private saldoSubject = new BehaviorSubject<number>(0);
  saldo$ = this.saldoSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
  ) {}

  setSaldo(saldo: number) {
    this.saldoSubject.next(saldo);
  }

  getSaldo(): number {
    return this.saldoSubject.value;
  }

  getSaldoFromBackend() {
    return this.http.get<number>(`${this.apiUrl}/saldo`);
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  crearUsuario(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }

  getPerfil(): Observable<Usuario> {
    const username = this.tokenService.getUsername();

    return this.http.get<any>(`${this.apiUrl}/username/${username}`).pipe(
      map((res) => ({
        id: res.id,
        nombre: res.nombre,
        correo: res.correo,
        nombreUsuario: res.nombreUsuario,
        fechaNacimiento: res.fechaNacimiento,
        saldo: res.saldo,
        estrellas: res.estrellas,
      })),
    );
  }

  actualizarPerfil(usuario: Usuario): Observable<Usuario> {
    return this.http.put<any>(`${this.apiUrl}/${usuario.id}`, usuario).pipe(
      map((res) => ({
        id: res.id,
        nombre: res.nombre,
        correo: res.correo,
        nombreUsuario: res.nombreUsuario,
        fechaNacimiento: res.fechaNacimiento,
        saldo: res.saldo,
        estrellas: res.estrellas,
      })),
    );
  }

  changePassword(currentPassword: string, newPassword: string) {
    return this.http.post(`${this.apiUrl}/change-password`, {
      currentPassword,
      newPassword,
    });
  }

  sumarSaldo(cantidad: number) {
    return this.http.post<number>(
      `${this.apiUrl}/saldo?cantidad=${cantidad}`,
      {},
    );
  }
}
