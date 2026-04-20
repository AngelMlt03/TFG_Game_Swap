import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { TokenService } from './token.service';
import { environment } from '../../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let tokenServiceSpy: jasmine.SpyObj<TokenService>;

  const mockResponse = { accessToken: 'fake-jwt-token' };
  const mockData = { email: 'test@test.com', password: '123' };
  const apiUrl = environment.authUrl;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('TokenService', ['setToken', 'removeToken']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: TokenService, useValue: spy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    tokenServiceSpy = TestBed.inject(TokenService) as jasmine.SpyObj<TokenService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe crearse correctamente', () => {
    expect(service).toBeTruthy();
  });

  it('login() debe realizar un POST y guardar el token al tener éxito', () => {
    service.login(mockData).subscribe((res) => {
      expect(res).toEqual(mockResponse);
      expect(tokenServiceSpy.setToken).toHaveBeenCalledWith(mockResponse.accessToken);
    });

    const req = httpMock.expectOne(`${apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockData);
    req.flush(mockResponse);
  });

  it('register() debe realizar un POST y guardar el token al tener éxito', () => {
    service.register(mockData).subscribe((res) => {
      expect(res).toEqual(mockResponse);
      expect(tokenServiceSpy.setToken).toHaveBeenCalledWith(mockResponse.accessToken);
    });

    const req = httpMock.expectOne(`${apiUrl}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockData);
    req.flush(mockResponse);
  });

  it('logout() debe eliminar el token a través del TokenService', () => {
    service.logout();
    expect(tokenServiceSpy.removeToken).toHaveBeenCalled();
  });
});