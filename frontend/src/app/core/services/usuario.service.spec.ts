import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UsuarioService } from './usuario.service';
import { Usuario } from '../models/usuario.model';
import { environment } from '../../../environments/environment';

describe('UsuarioService', () => {
  let service: UsuarioService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/usuarios`;

  const mockUsuarios: Usuario[] = [
    { id: 1, nombre: 'Usuario 1', email: 'user1@test.com' } as any,
    { id: 2, nombre: 'Usuario 2', email: 'user2@test.com' } as any
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UsuarioService]
    });
    service = TestBed.inject(UsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('debe crearse correctamente', () => {
    expect(service).toBeTruthy();
  });

  it('getUsuarios() debe retornar una lista de usuarios mediante GET', () => {
    service.getUsuarios().subscribe((usuarios) => {
      expect(usuarios.length).toBe(2);
      expect(usuarios).toEqual(mockUsuarios);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockUsuarios);
  });

  it('crearUsuario() debe enviar un nuevo usuario mediante POST y retornarlo', () => {
    const nuevoUsuario: Usuario = { nombre: 'Nuevo', email: 'nuevo@test.com' } as any;

    service.crearUsuario(nuevoUsuario).subscribe((usuario) => {
      expect(usuario).toEqual(nuevoUsuario);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nuevoUsuario);
    req.flush(nuevoUsuario);
  });
});