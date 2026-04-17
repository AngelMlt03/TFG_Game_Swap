import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {

  private KEY = 'token';

  setToken(token: string) {
    localStorage.setItem(this.KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.KEY);
  }

  removeToken() {
    localStorage.removeItem(this.KEY);
  }

  isLogged(): boolean {
    return !!this.getToken();
  }

  getUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
  }
}