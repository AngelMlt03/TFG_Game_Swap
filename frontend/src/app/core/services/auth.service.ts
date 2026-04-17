import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TokenService } from './token.service';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly API = `${environment.authUrl}`;

  constructor(
    private readonly http: HttpClient,
    private readonly tokenService: TokenService
  ) {}

  login(data: any) {
    return this.http.post<any>(`${this.API}/login`, data)
      .pipe(
        tap(res => this.tokenService.setToken(res.accessToken))
      );
  }

  register(data: any) {
    return this.http.post<any>(`${this.API}/register`, data)
      .pipe(
        tap(res => this.tokenService.setToken(res.accessToken))
      );
  }

  logout() {
    this.tokenService.removeToken();
  }
}