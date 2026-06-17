import { TestBed } from '@angular/core/testing';
import { TokenService } from './token.service';

describe('TokenService', () => {
  let service: TokenService;
  const KEY = 'token';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenService]
    });
    service = TestBed.inject(TokenService);
    localStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('debe crearse correctamente', () => {
    expect(service).toBeTruthy();
  });

  it('setToken() debe guardar el token en localStorage', () => {
    const token = 'mi-token-de-prueba';
    service.setToken(token);
    expect(localStorage.getItem(KEY)).toBe(token);
  });

  it('getToken() debe obtener el token desde localStorage', () => {
    const token = 'mi-token-de-prueba';
    localStorage.setItem(KEY, token);
    expect(service.getToken()).toBe(token);
  });

  it('removeToken() debe eliminar el token de localStorage', () => {
    localStorage.setItem(KEY, 'token-a-borrar');
    service.removeToken();
    expect(localStorage.getItem(KEY)).toBeNull();
  });

  it('isLogged() debe retornar false si no existe un token', () => {
    expect(service.isLogged()).toBeFalse();
  });

  it('getUsername() debe retornar null si no hay token', () => {
    expect(service.getUsername()).toBeNull();
  });

  it('getUsername() debe decodificar el payload y retornar el "sub"', () => {
    const payload = { sub: 'usuario_test', exp: 123456789 };
    const payloadEncoded = btoa(JSON.stringify(payload));
    const fakeJwt = `header.${payloadEncoded}.signature`;
    
    localStorage.setItem(KEY, fakeJwt);
    
    expect(service.getUsername()).toBe('usuario_test');
  });
});