/* sonar-ignore */
/* istanbul ignore file */
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {

  private readonly KEY = 'token';

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

    const token = this.getToken();

    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
  }
}