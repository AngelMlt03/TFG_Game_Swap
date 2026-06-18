import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { ModalIntercambioComponent } from './modal-intercambio.component';

import { IgdbService } from '../../../../core/services/igdb.service';
import { PostService } from '../../../../core/services/post.service';
import { AlertService } from '../../../../core/services/alert.service';
import { Router } from '@angular/router';

describe('ModalIntercambioComponent', () => {
  let component: ModalIntercambioComponent;
  let fixture: ComponentFixture<ModalIntercambioComponent>;

  let igdbService: any;
  let postService: any;
  let alertService: any;
  let router: any;

  beforeEach(async () => {
    igdbService = {
      buscarJuegos: jasmine
        .createSpy()
        .and.returnValue(of([{ id: 1, nombre: 'Zelda', imagen: 'img' }])),
      buscarPlataformas: jasmine
        .createSpy()
        .and.returnValue(of([{ nombre: 'PS5' }])),
    };

    postService = {
      crearIntercambio: jasmine.createSpy().and.returnValue(of({})),
      actualizarIntercambio: jasmine.createSpy().and.returnValue(of({})),
      convertirVentaAIntercambio: jasmine.createSpy().and.returnValue(of({})),
      existeIntercambioSugerido: jasmine.createSpy().and.returnValue(of(true)),
    };

    alertService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
    };

    router = {
      navigate: jasmine.createSpy(),
    };

    await TestBed.configureTestingModule({
      imports: [ModalIntercambioComponent],
      providers: [
        { provide: IgdbService, useValue: igdbService },
        { provide: PostService, useValue: postService },
        { provide: AlertService, useValue: alertService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalIntercambioComponent);
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
      imagen: 'img',

      nombreProductoIntercambio: 'Mario',
      plataformaIntercambio: 'Switch',
      estadoIntercambio: 'USADO',
      imagenIntercambio: 'img2',

      idApi: 10,
      idApiIntercambio: 20,
      descripcion: 'desc',
    };

    component.ngOnChanges({
      postEditar: {
        currentValue: component.postEditar,
        previousValue: null,
        firstChange: true,
        isFirstChange: () => true,
      },
    });

    expect(component.nombreJuego).toBe('Zelda');
    expect(component.nombreJuegoIntercambio).toBe('Mario');
  });

  it('ngOnChanges modo conversion', () => {
    component.postEditar = {
      modoConversion: true,
      idVentaOriginal: 99,
    };

    component.ngOnChanges({});

    expect(component.modoConversion).toBeTrue();
    expect(component.idVentaOriginal).toBe(99);
  });

  it('buscarJuego', () => {
    component.nombreJuego = 'Zelda';

    component.buscarJuego();

    expect(igdbService.buscarJuegos).toHaveBeenCalled();
  });

  it('buscarJuego menos de 3 caracteres', () => {
    component.nombreJuego = 'ab';

    component.buscarJuego();

    expect(igdbService.buscarJuegos).not.toHaveBeenCalled();
  });

  it('seleccionarJuego', () => {
    component.seleccionarJuego({
      id: 1,
      nombre: 'Zelda',
      imagen: 'img',
    });

    expect(component.nombreJuego).toBe('Zelda');
    expect(component.focusedJuego).toBeFalse();
  });

  it('buscarPlataforma', () => {
    component.plataforma = 'PS5';

    component.buscarPlataforma();

    expect(igdbService.buscarPlataformas).toHaveBeenCalled();
  });

  it('seleccionarPlataforma', () => {
    component.seleccionarPlataforma({ nombre: 'PS5' });

    expect(component.plataforma).toBe('PS5');
  });

  it('buscarJuegoIntercambio', () => {
    component.nombreJuegoIntercambio = 'Mario';

    component.buscarJuegoIntercambio();

    expect(igdbService.buscarJuegos).toHaveBeenCalled();
  });

  it('seleccionarJuegoIntercambio', () => {
    component.seleccionarJuegoIntercambio({
      id: 2,
      nombre: 'Mario',
      imagen: 'img',
    });

    expect(component.nombreJuegoIntercambio).toBe('Mario');
  });

  it('buscarPlataformaIntercambio', () => {
    component.plataformaIntercambio = 'PS5';

    component.buscarPlataformaIntercambio();

    expect(igdbService.buscarPlataformas).toHaveBeenCalled();
  });

  it('seleccionarEstado', () => {
    component.seleccionarEstado({ value: 'NUEVO' });

    expect(component.estado).toBe('NUEVO');
  });

  it('seleccionarEstadoIntercambio', () => {
    component.seleccionarEstadoIntercambio({ value: 'USADO' });

    expect(component.estadoIntercambio).toBe('USADO');
  });

  it('formularioValido true', () => {
    component.juegoSeleccionado = {};
    component.plataforma = 'PS5';
    component.estado = 'NUEVO';

    component.juegoIntercambioSeleccionado = {};
    component.plataformaIntercambio = 'PS5';
    component.estadoIntercambio = 'USADO';

    expect(component.formularioValido()).toBeTrue();
  });

  it('formularioValido false', () => {
    expect(component.formularioValido()).toBeFalse();
  });

  it('publicar abre modal', () => {
    component.publicar();

    expect(component.mostrarModalDireccion).toBeTrue();
  });

  it('confirmarPublicar crear', () => {
    spyOn(component.close, 'emit');

    component.juegoSeleccionado = { id: 1 };
    component.juegoIntercambioSeleccionado = { id: 2 };

    component.confirmarPublicar();

    expect(postService.crearIntercambio).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
    expect(component.close.emit).toHaveBeenCalledWith(true);
  });

  it('confirmarPublicar editar', () => {
    component.modoEdicion = true;
    component.postEditar = { id: 5 };

    component.juegoSeleccionado = { id: 1 };
    component.juegoIntercambioSeleccionado = { id: 2 };

    component.confirmarPublicar();

    expect(postService.actualizarIntercambio).toHaveBeenCalled();
  });

  it('confirmarPublicar conversion', () => {
    component.modoConversion = true;
    component.idVentaOriginal = 10;

    component.juegoSeleccionado = { id: 1 };
    component.juegoIntercambioSeleccionado = { id: 2 };

    component.confirmarPublicar();

    expect(postService.convertirVentaAIntercambio).toHaveBeenCalled();
  });

  it('cancelarPublicar', () => {
    component.mostrarModalDireccion = true;

    component.cancelarPublicar();

    expect(component.mostrarModalDireccion).toBeFalse();
  });

  it('limpiarJuego', () => {
    component.nombreJuego = 'Zelda';
    component.juegoSeleccionado = {};

    component.limpiarJuego();

    expect(component.nombreJuego).toBe('');
    expect(component.juegoSeleccionado).toBeNull();
  });

  it('limpiarJuegoIntercambio', () => {
    component.nombreJuegoIntercambio = 'Mario';
    component.juegoIntercambioSeleccionado = {};

    component.limpiarJuegoIntercambio();

    expect(component.nombreJuegoIntercambio).toBe('');
    expect(component.juegoIntercambioSeleccionado).toBeNull();
  });

  it('getEstadoLabel', () => {
    expect(component.getEstadoLabel('NUEVO')).toBe('Nuevo');
  });

  it('getEstadoIntercambioLabel', () => {
    expect(component.getEstadoIntercambioLabel('USADO')).toBe('Usado');
  });

  it('cambiarAVenta', () => {
    spyOn(component.convertirAVenta, 'emit');
    spyOn(component.close, 'emit');

    component.postEditar = { id: 1 };

    component.cambiarAVenta();

    expect(component.convertirAVenta.emit).toHaveBeenCalled();
    expect(component.close.emit).toHaveBeenCalled();
  });

  it('comprobarIntercambioExistente true', () => {
    component.nombreJuego = 'Zelda';
    component.nombreJuegoIntercambio = 'Mario';

    component.comprobarIntercambioExistente();

    expect(postService.existeIntercambioSugerido).toHaveBeenCalled();
    expect(component.mostrarAvisoIntercambio).toBeTrue();
  });

  it('comprobarIntercambioExistente sin nombres', () => {
    component.nombreJuego = '';
    component.nombreJuegoIntercambio = '';

    component.comprobarIntercambioExistente();

    expect(component.mostrarAvisoIntercambio).toBeFalse();
  });

  it('onBlur methods', fakeAsync(() => {
    component.focusedJuego = true;
    component.onBlurJuego();
    tick(200);
    expect(component.focusedJuego).toBeFalse();
  }));
});
