import { TestBed } from '@angular/core/testing';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { PostService } from './post.service';
import { environment } from '../../../environments/environment';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.apiUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PostService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('buscarPosts con filtros', () => {
    service
      .buscarPosts({
        videojuego: 'Zelda',
        tipo: 'VENTA',
        estado: 'NUEVO',
        plataforma: 'Switch',
      })
      .subscribe();

    const req = httpMock.expectOne((r) => r.url === `${apiUrl}/busqueda`);

    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('nombre')).toBe('Zelda');
    expect(req.request.params.get('tipo')).toBe('VENTA');
    expect(req.request.params.get('estado')).toBe('NUEVO');
    expect(req.request.params.get('plataforma')).toBe('Switch');

    req.flush([]);
  });

  it('comprar', () => {
    let saldo = 0;

    service.comprar(1).subscribe((s) => (saldo = s));

    const req = httpMock.expectOne(`${apiUrl}/transaccion/comprar/1`);

    expect(req.request.method).toBe('POST');

    req.flush({ saldo: 150 });

    expect(saldo).toBe(150);
  });

  it('intercambiar', () => {
    service.intercambiar(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/transaccion/intercambiar/1`);

    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  it('crearVenta', () => {
    service.crearVenta({ a: 1 }).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-venta`);

    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  it('crearIntercambio', () => {
    service.crearIntercambio({}).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-intercambio`);

    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  it('actualizarVenta', () => {
    service.actualizarVenta(1, {}).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-venta/1`);

    expect(req.request.method).toBe('PUT');

    req.flush({});
  });

  it('eliminarVenta', () => {
    service.eliminarVenta(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-venta/1`);

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  it('actualizarIntercambio', () => {
    service.actualizarIntercambio(1, {}).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-intercambio/1`);

    expect(req.request.method).toBe('PUT');

    req.flush({});
  });

  it('eliminarIntercambio', () => {
    service.eliminarIntercambio(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/posts-intercambio/1`);

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  it('historial compras', () => {
    service.getHistorialCompras().subscribe();

    const req = httpMock.expectOne(`${apiUrl}/historial/compras`);

    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  it('historial ventas', () => {
    service.getHistorialVentas().subscribe();

    const req = httpMock.expectOne(`${apiUrl}/historial/ventas`);

    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  it('historial intercambios', () => {
    service.getHistorialIntercambios().subscribe();

    const req = httpMock.expectOne(`${apiUrl}/historial/intercambios`);

    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  it('guardarPost', () => {
    service.guardarPost(1, 'VENTA').subscribe();

    const req = httpMock.expectOne(`${apiUrl}/guardados`);

    expect(req.request.method).toBe('POST');

    req.flush({});
  });

  it('estaGuardado', () => {
    service.estaGuardado(1, 'VENTA').subscribe();

    const req = httpMock.expectOne(`${apiUrl}/guardados/existe/1/VENTA`);

    expect(req.request.method).toBe('GET');

    req.flush(true);
  });

  it('eliminarGuardado', () => {
    service.eliminarGuardado(1, 'VENTA').subscribe();

    const req = httpMock.expectOne(`${apiUrl}/guardados/1/VENTA`);

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  it('ventas guardadas', () => {
    service.getVentasGuardadas().subscribe();

    httpMock.expectOne(`${apiUrl}/guardados/ventas`).flush([]);
  });

  it('intercambios guardados', () => {
    service.getIntercambiosGuardados().subscribe();

    httpMock.expectOne(`${apiUrl}/guardados/intercambios`).flush([]);
  });

  it('getCompraById', () => {
    service.getCompraById(1).subscribe();

    httpMock.expectOne(`${apiUrl}/reviews/compra/1`).flush({});
  });

  it('getIntercambioById', () => {
    service.getIntercambioById(1).subscribe();

    httpMock.expectOne(`${apiUrl}/reviews/intercambio/1`).flush({});
  });

  it('agregarCarrito', () => {
    service.agregarCarrito(1).subscribe();

    httpMock.expectOne(`${apiUrl}/carrito/1`).flush({});
  });

  it('eliminarCarrito', () => {
    service.eliminarCarrito(1).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/carrito/1`);

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  it('estaEnCarrito', () => {
    service.estaEnCarrito(1).subscribe();

    httpMock.expectOne(`${apiUrl}/carrito/exists/1`).flush(true);
  });

  it('obtenerCarrito', () => {
    service.obtenerCarrito().subscribe();

    httpMock.expectOne(`${apiUrl}/carrito`).flush([]);
  });

  it('vaciarCarrito', () => {
    service.vaciarCarrito().subscribe();

    const req = httpMock.expectOne(`${apiUrl}/carrito/vaciar`);

    expect(req.request.method).toBe('DELETE');

    req.flush({});
  });

  it('obtenerPrecioCarrito', () => {
    service.obtenerPrecioCarrito().subscribe();

    httpMock.expectOne(`${apiUrl}/carrito/precio`).flush(100);
  });

  it('convertirVentaAIntercambio', () => {
    service.convertirVentaAIntercambio(1, {}).subscribe();

    httpMock.expectOne(`${apiUrl}/posts-venta/1/convertir`).flush({});
  });

  it('convertirIntercambioAVenta', () => {
    service.convertirIntercambioAVenta(1, {}).subscribe();

    httpMock.expectOne(`${apiUrl}/posts-intercambio/1/convertir`).flush({});
  });

  it('existeIntercambioSugerido', () => {
    service.existeIntercambioSugerido('Zelda', 'Mario').subscribe();

    const req = httpMock.expectOne(
      (r) => r.url === `${apiUrl}/posts-intercambio/intercambio-sugerido`,
    );

    expect(req.request.params.get('tuJuego')).toBe('Zelda');

    expect(req.request.params.get('juegoBuscado')).toBe('Mario');

    req.flush(true);
  });

  it('notificarActualizacionCarrito', () => {
    let recibido = false;

    service.carritoActualizado$.subscribe(() => {
      recibido = true;
    });

    service.notificarActualizacionCarrito();

    expect(recibido).toBeTrue();
  });
});
