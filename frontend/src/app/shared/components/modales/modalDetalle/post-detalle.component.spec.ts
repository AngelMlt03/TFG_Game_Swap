import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError, BehaviorSubject } from 'rxjs';
import { SimpleChange } from '@angular/core';

import { PostDetalleComponent } from './post-detalle.component';

import { PostService } from '../../../../core/services/post.service';
import { UsuarioService } from '../../../../core/services/usuario.service';
import { AlertService } from '../../../../core/services/alert.service';
import { IgdbService } from '../../../../core/services/igdb.service';
import { Router } from '@angular/router';

describe('PostDetalleComponent', () => {
  let component: PostDetalleComponent;
  let fixture: ComponentFixture<PostDetalleComponent>;

  let postService: any;
  let usuarioService: any;
  let alertService: any;
  let igdbService: any;
  let router: any;

  beforeEach(async () => {
    postService = {
      estaGuardado: jasmine.createSpy().and.returnValue(of(true)),
      estaEnCarrito: jasmine.createSpy().and.returnValue(of(true)),
      comprar: jasmine.createSpy().and.returnValue(of(80)),
      intercambiar: jasmine.createSpy().and.returnValue(of(null)),
      eliminarGuardado: jasmine.createSpy().and.returnValue(of(null)),
      guardarPost: jasmine.createSpy().and.returnValue(of(null)),
      eliminarCarrito: jasmine.createSpy().and.returnValue(of(null)),
      agregarCarrito: jasmine.createSpy().and.returnValue(of(null)),
      notificarActualizacionCarrito: jasmine.createSpy(),
    };

    usuarioService = {
      saldo$: new BehaviorSubject<number>(100),
      getSaldo: jasmine.createSpy().and.returnValue(100),
      getSaldoFromBackend: jasmine.createSpy().and.returnValue(of(100)),
      setSaldo: jasmine.createSpy(),
    };

    alertService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
    };

    igdbService = {
      getGameDetails: jasmine.createSpy().and.returnValue(
        of([
          {
            id: 1,
            name: 'Game',
          },
        ]),
      ),
    };

    router = {
      navigate: jasmine.createSpy(),
    };

    await TestBed.configureTestingModule({
      imports: [PostDetalleComponent],
      providers: [
        { provide: PostService, useValue: postService },
        { provide: UsuarioService, useValue: usuarioService },
        { provide: AlertService, useValue: alertService },
        { provide: IgdbService, useValue: igdbService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetalleComponent);
    component = fixture.componentInstance;

    component.post = {
      id: 1,
      tipo: 'VENTA',
      idApi: 10,
      precio: 20,
      nombreUsuario: 'angel',
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit saldo > 0', () => {
    component.ngOnInit();

    expect(usuarioService.getSaldo).toHaveBeenCalled();
  });

  it('ngOnInit saldo = 0', () => {
    usuarioService.getSaldo.and.returnValue(0);

    component.ngOnInit();

    expect(usuarioService.getSaldoFromBackend).toHaveBeenCalled();
    expect(usuarioService.setSaldo).toHaveBeenCalled();
  });

  it('ngOnChanges venta', () => {
    component.ngOnChanges({
      post: new SimpleChange(null, component.post, true),
    });

    expect(postService.estaGuardado).toHaveBeenCalled();
    expect(postService.estaEnCarrito).toHaveBeenCalled();
    expect(igdbService.getGameDetails).toHaveBeenCalled();
  });

  it('ngOnChanges con imagenes externas', () => {
    component.externPostImg = 'img.jpg';
    component.externPostIntercambioImg = 'img2.jpg';

    component.ngOnChanges({
      post: new SimpleChange(null, component.post, true),
    });

    expect(component.post.imagen).toBe('img.jpg');
    expect(component.post.imagenIntercambio).toBe('img2.jpg');
  });

  it('cerrar emite evento', () => {
    spyOn(component.close, 'emit');

    component.cerrar();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('tieneSaldo true', () => {
    component.saldo = 100;

    expect(component.tieneSaldo()).toBeTrue();
  });

  it('tieneSaldo false', () => {
    component.saldo = 0;

    expect(component.tieneSaldo()).toBeFalse();
  });

  it('irASaldo', () => {
    spyOn(component, 'cerrar');

    component.irASaldo();

    expect(component.cerrar).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalled();
  });

  it('comprar abre modal', () => {
    component.comprar();

    expect(component.mostrarModalDireccion).toBeTrue();
    expect(component.tipoAccionDireccion).toBe('COMPRA');
  });

  it('cancelarCompra', () => {
    component.mostrarModalDireccion = true;

    component.cancelarCompra();

    expect(component.mostrarModalDireccion).toBeFalse();
  });

  it('intercambiar abre modal', () => {
    component.intercambiar();

    expect(component.mostrarModalDireccion).toBeTrue();
    expect(component.tipoAccionDireccion).toBe('INTERCAMBIO');
  });

  it('cancelarIntercambio', () => {
    component.mostrarModalDireccion = true;

    component.cancelarIntercambio();

    expect(component.mostrarModalDireccion).toBeFalse();
  });

  it('confirmarCompra success', () => {
    spyOn(component.accionRealizada, 'emit');
    spyOn(component, 'cerrar');

    component.confirmarCompra();

    expect(usuarioService.setSaldo).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
    expect(component.accionRealizada.emit).toHaveBeenCalled();
    expect(component.cerrar).toHaveBeenCalled();
  });

  it('confirmarCompra error', () => {
    postService.comprar.and.returnValue(throwError(() => new Error()));

    component.confirmarCompra();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('confirmarCompra sin id', () => {
    component.post = {};

    component.confirmarCompra();

    expect(postService.comprar).not.toHaveBeenCalled();
  });

  it('confirmarIntercambio success', () => {
    spyOn(component.accionRealizada, 'emit');
    spyOn(component, 'cerrar');

    component.confirmarIntercambio();

    expect(alertService.success).toHaveBeenCalled();
    expect(component.accionRealizada.emit).toHaveBeenCalled();
  });

  it('confirmarIntercambio error', () => {
    postService.intercambiar.and.returnValue(throwError(() => new Error()));

    component.confirmarIntercambio();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('guardarPost nuevo', () => {
    component.guardado = false;

    component.guardarPost();

    expect(postService.guardarPost).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
  });

  it('guardarPost eliminar', () => {
    component.guardado = true;

    component.guardarPost();

    expect(postService.eliminarGuardado).toHaveBeenCalled();
  });

  it('guardarPost error guardar', () => {
    component.guardado = false;

    postService.guardarPost.and.returnValue(throwError(() => new Error()));

    component.guardarPost();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('guardarPost error eliminar', () => {
    component.guardado = true;

    postService.eliminarGuardado.and.returnValue(throwError(() => new Error()));

    component.guardarPost();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('toggleCarrito añadir', () => {
    component.enCarrito = false;

    component.toggleCarrito();

    expect(postService.agregarCarrito).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
  });

  it('toggleCarrito eliminar', () => {
    component.enCarrito = true;

    component.toggleCarrito();

    expect(postService.eliminarCarrito).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
  });

  it('toggleCarrito sin id', () => {
    component.post = {};

    component.toggleCarrito();

    expect(postService.agregarCarrito).not.toHaveBeenCalled();
  });

  it('abrirImagen', () => {
    component.abrirImagen('test.jpg');

    expect(component.imagenExpandida).toBe('test.jpg');
  });

  it('cerrarImagen', () => {
    component.imagenExpandida = 'test.jpg';

    component.cerrarImagen();

    expect(component.imagenExpandida).toBeNull();
  });
});
