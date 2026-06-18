import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { ModalVentaComponent } from './modal-venta.component';

import { IgdbService } from '../../../../core/services/igdb.service';
import { PostService } from '../../../../core/services/post.service';
import { AlertService } from '../../../../core/services/alert.service';
import { Router } from '@angular/router';

describe('ModalVentaComponent', () => {
  let component: ModalVentaComponent;
  let fixture: ComponentFixture<ModalVentaComponent>;

  let igdbService: any;
  let postService: any;
  let alertService: any;
  let router: any;

  beforeEach(async () => {
    igdbService = {
      buscarJuegos: jasmine.createSpy().and.returnValue(of([])),
      buscarPlataformas: jasmine.createSpy().and.returnValue(of([])),
    };

    postService = {
      crearVenta: jasmine.createSpy().and.returnValue(of({})),
      actualizarVenta: jasmine.createSpy().and.returnValue(of({})),
      convertirIntercambioAVenta: jasmine.createSpy().and.returnValue(of({})),
    };

    alertService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
    };

    router = {
      navigate: jasmine.createSpy(),
    };

    await TestBed.configureTestingModule({
      imports: [ModalVentaComponent],
      providers: [
        { provide: IgdbService, useValue: igdbService },
        { provide: PostService, useValue: postService },
        { provide: AlertService, useValue: alertService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnChanges carga datos de edición', () => {
    component.postEditar = {
      id: 1,
      nombreProducto: 'Zelda',
      plataforma: 'Switch',
      estado: 'NUEVO',
      precio: 50,
      descripcion: 'desc',
      imagen: 'img',
      idProducto: 99,
      idApi: 123,
    };

    component.ngOnChanges({
      postEditar: {} as any,
    });

    expect(component.nombreJuego).toBe('Zelda');
    expect(component.plataforma).toBe('Switch');
    expect(component.estado).toBe('NUEVO');
    expect(component.precio).toBe(50);
  });

  it('ngOnChanges activa modo conversión', () => {
    component.postEditar = {
      modoConversion: true,
      idIntercambioOriginal: 10,
    };

    component.ngOnChanges({
      postEditar: {} as any,
    });

    expect(component.modoConversion).toBeTrue();
    expect(component.idIntercambioOriginal).toBe(10);
  });

  it('buscarJuego limpia resultados si texto corto', () => {
    component.nombreJuego = 'a';

    component.buscarJuego();

    expect(component.resultadosBusqueda.length).toBe(0);
  });

  it('buscarJuego obtiene resultados', () => {
    igdbService.buscarJuegos.and.returnValue(of([{ nombre: 'Zelda' }]));

    component.nombreJuego = 'Zelda';

    component.buscarJuego();

    expect(igdbService.buscarJuegos).toHaveBeenCalled();
    expect(component.loadingJuego).toBeFalse();
  });

  it('buscarJuego controla error', () => {
    igdbService.buscarJuegos.and.returnValue(throwError(() => new Error()));

    component.nombreJuego = 'Zelda';

    component.buscarJuego();

    expect(component.loadingJuego).toBeFalse();
  });

  it('seleccionarJuego', () => {
    component.seleccionarJuego({
      nombre: 'Zelda',
      imagen: 'img',
    });

    expect(component.nombreJuego).toBe('Zelda');
    expect(component.imagen).toBe('img');
  });

  it('buscarPlataforma limpia resultados', () => {
    component.plataforma = '';

    component.buscarPlataforma();

    expect(component.sugerenciasPlataforma.length).toBe(0);
  });

  it('buscarPlataforma obtiene resultados', () => {
    igdbService.buscarPlataformas.and.returnValue(of([{ nombre: 'Switch' }]));

    component.plataforma = 'S';

    component.buscarPlataforma();

    expect(igdbService.buscarPlataformas).toHaveBeenCalled();
  });

  it('buscarPlataforma controla error', () => {
    igdbService.buscarPlataformas.and.returnValue(
      throwError(() => new Error()),
    );

    component.plataforma = 'Switch';

    component.buscarPlataforma();

    expect(component.loadingPlataforma).toBeFalse();
  });

  it('seleccionarPlataforma', () => {
    component.seleccionarPlataforma({
      nombre: 'PS5',
    });

    expect(component.plataforma).toBe('PS5');
    expect(component.focusedPlataforma).toBeFalse();
  });

  it('seleccionarEstado', () => {
    component.seleccionarEstado({
      value: 'USADO',
    });

    expect(component.estado).toBe('USADO');
    expect(component.focusedEstado).toBeFalse();
  });

  it('formularioValido false', () => {
    expect(component.formularioValido()).toBeFalse();
  });

  it('formularioValido true', () => {
    component.juegoSeleccionado = {};
    component.plataforma = 'PS5';
    component.estado = 'NUEVO';
    component.precio = 10;

    expect(component.formularioValido()).toBeTrue();
  });

  it('publicar crea venta', () => {
    spyOn(component.close, 'emit');

    component.juegoSeleccionado = { id: 1 };
    component.nombreJuego = 'Zelda';
    component.plataforma = 'Switch';
    component.estado = 'NUEVO';
    component.precio = 20;

    component.publicar();

    expect(postService.crearVenta).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
    expect(component.close.emit).toHaveBeenCalledWith(true);
  });

  it('publicar controla error creando venta', () => {
    postService.crearVenta.and.returnValue(throwError(() => new Error()));

    component.juegoSeleccionado = { id: 1 };
    component.nombreJuego = 'Zelda';
    component.plataforma = 'Switch';
    component.estado = 'NUEVO';
    component.precio = 20;

    component.publicar();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('publicar actualiza venta', () => {
    component.modoEdicion = true;

    component.postEditar = {
      id: 5,
    };

    component.juegoSeleccionado = { id: 1 };
    component.nombreJuego = 'Juego';
    component.plataforma = 'PS5';
    component.estado = 'NUEVO';
    component.precio = 10;

    component.publicar();

    expect(postService.actualizarVenta).toHaveBeenCalled();
  });

  it('publicar controla error actualizando', () => {
    postService.actualizarVenta.and.returnValue(throwError(() => new Error()));

    component.modoEdicion = true;

    component.postEditar = {
      id: 5,
    };

    component.juegoSeleccionado = { id: 1 };
    component.nombreJuego = 'Juego';
    component.plataforma = 'PS5';
    component.estado = 'NUEVO';
    component.precio = 10;

    component.publicar();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('publicar convierte intercambio a venta', () => {
    component.modoConversion = true;
    component.idIntercambioOriginal = 7;

    component.juegoSeleccionado = { id: 1 };
    component.nombreJuego = 'Juego';
    component.plataforma = 'PS5';
    component.estado = 'NUEVO';
    component.precio = 10;

    component.publicar();

    expect(postService.convertirIntercambioAVenta).toHaveBeenCalled();
  });

  it('focus juego', () => {
    component.onFocusJuego();
    expect(component.focusedJuego).toBeTrue();
  });

  it('focus plataforma', () => {
    component.onFocusPlataforma();
    expect(component.focusedPlataforma).toBeTrue();
  });

  it('getEstadoLabel devuelve texto', () => {
    expect(component.getEstadoLabel('NUEVO')).toBe('Nuevo');
  });

  it('getEstadoLabel devuelve vacío', () => {
    expect(component.getEstadoLabel('XXX')).toBe('');
  });

  it('blur juego', fakeAsync(() => {
    component.focusedJuego = true;

    component.onBlurJuego();

    tick(150);

    expect(component.focusedJuego).toBeFalse();
  }));

  it('blur plataforma', fakeAsync(() => {
    component.focusedPlataforma = true;

    component.onBlurPlataforma();

    tick(150);

    expect(component.focusedPlataforma).toBeFalse();
  }));

  it('blur estado', fakeAsync(() => {
    component.focusedEstado = true;

    component.onBlurEstado();

    tick(150);

    expect(component.focusedEstado).toBeFalse();
  }));

  it('cambiarAIntercambio', () => {
    spyOn(component.convertirAIntercambio, 'emit');
    spyOn(component.close, 'emit');

    component.postEditar = {
      id: 5,
    };

    component.idJuego = '10';
    component.nombreJuego = 'Juego';
    component.plataforma = 'PS5';
    component.estado = 'USADO';
    component.descripcion = 'desc';
    component.imagen = 'img';

    component.cambiarAIntercambio();

    expect(component.convertirAIntercambio.emit).toHaveBeenCalled();

    expect(component.close.emit).toHaveBeenCalledWith(false);
  });
});
