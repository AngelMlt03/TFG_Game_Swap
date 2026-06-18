import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { HistorialComponent } from './historial.component';

import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { provideHttpClient } from '@angular/common/http';

describe('HistorialComponent', () => {
  let component: HistorialComponent;
  let fixture: ComponentFixture<HistorialComponent>;

  let postService: any;
  let igdbService: any;

  beforeEach(async () => {
    postService = {
      getHistorialCompras: jasmine.createSpy().and.returnValue(
        of([
          {
            idApiProducto: 1,
            nombreProducto: 'Juego Compra',
            precio: 20,
          },
        ]),
      ),

      getHistorialVentas: jasmine.createSpy().and.returnValue(
        of([
          {
            idApiProducto: 2,
            nombreProducto: 'Juego Venta',
            precio: 30,
          },
        ]),
      ),

      getHistorialIntercambios: jasmine.createSpy().and.returnValue(
        of([
          {
            idApiProductoOfrecido: 3,
            idApiProductoDeseado: 4,
          },
        ]),
      ),
    };

    igdbService = {
      getCover: jasmine.createSpy().and.returnValue(of('cover.jpg')),
    };

    await TestBed.configureTestingModule({
      imports: [HistorialComponent],
      providers: [
        provideHttpClient(),
        { provide: PostService, useValue: postService },
        { provide: IgdbService, useValue: igdbService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HistorialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit carga compras, ventas e intercambios', () => {
    expect(postService.getHistorialCompras).toHaveBeenCalled();
    expect(postService.getHistorialVentas).toHaveBeenCalled();
    expect(postService.getHistorialIntercambios).toHaveBeenCalled();
  });

  it('cargarCompras', () => {
    component.compras = [];

    component.cargarCompras();

    expect(component.compras.length).toBe(1);
  });

  it('cargarVentas', () => {
    component.ventas = [];

    component.cargarVentas();

    expect(component.ventas.length).toBe(1);
  });

  it('cargarIntercambios', () => {
    component.intercambios = [];

    component.cargarIntercambios();

    expect(component.intercambios.length).toBe(1);
  });

  it('cargarImagen', () => {
    component.cargarImagen(10);

    expect(component.imagenes[10]).toBe('cover.jpg');
  });

  it('cargarImagen no vuelve a cargar si ya existe', () => {
    component.imagenes[10] = 'ya-existe.jpg';

    component.cargarImagen(10);

    expect(component.imagenes[10]).toBe('ya-existe.jpg');
  });

  it('getImagen devuelve imagen existente', () => {
    component.imagenes[1] = 'imagen.jpg';

    expect(component.getImagen(1)).toBe('imagen.jpg');
  });

  it('getImagen devuelve placeholder', () => {
    expect(component.getImagen(999)).toBe('no-image.png');
  });

  it('verDetalle VENTA', () => {
    const post: any = {
      idApiProducto: 1,
    };

    component.imagenes[1] = 'venta.jpg';

    component.verDetalle(post, 'VENTA');

    expect(component.modalDetalleOpen).toBeTrue();
    expect(component.postDetalle).toBe(post);
    expect(post.imagen).toBe('venta.jpg');
  });

  it('verDetalle INTERCAMBIO', () => {
    const post: any = {
      idApiProductoOfrecido: 1,
      idApiProductoDeseado: 2,
    };

    component.imagenes[1] = 'ofrecido.jpg';
    component.imagenes[2] = 'deseado.jpg';

    component.verDetalle(post, 'INTERCAMBIO');

    expect(component.modalDetalleOpen).toBeTrue();
    expect(post.imagen).toBe('ofrecido.jpg');
    expect(post.imagenIntercambio).toBe('deseado.jpg');
  });

  it('cerrarDetalle', () => {
    component.modalDetalleOpen = true;
    component.postDetalle = {};

    component.cerrarDetalle();

    expect(component.modalDetalleOpen).toBeFalse();
    expect(component.postDetalle).toBeNull();
  });

  it('abrirReview VENTA', () => {
    const post: any = {
      idApiProducto: 1,
    };

    component.imagenes[1] = 'venta.jpg';

    component.abrirReview(post, 'VENTA');

    expect(component.modalReviewOpen).toBeTrue();
    expect(component.postReview).toBe(post);
    expect(component.tipoReview).toBe('VENTA');
  });

  it('abrirReview INTERCAMBIO', () => {
    const post: any = {
      idApiProductoOfrecido: 1,
      idApiProductoDeseado: 2,
    };

    component.imagenes[1] = 'ofrecido.jpg';
    component.imagenes[2] = 'deseado.jpg';

    component.abrirReview(post, 'INTERCAMBIO');

    expect(component.modalReviewOpen).toBeTrue();
    expect(component.tipoReview).toBe('INTERCAMBIO');
    expect(post.imagenIntercambio).toBe('deseado.jpg');
  });

  it('cerrarReview', () => {
    component.modalReviewOpen = true;
    component.postReview = {};

    component.cerrarReview();

    expect(component.modalReviewOpen).toBeFalse();
    expect(component.postReview).toBeNull();
  });
});
