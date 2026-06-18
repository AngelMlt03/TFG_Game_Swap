import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, Subject, throwError } from 'rxjs';
import { Router } from '@angular/router';

import { ModalCarritoComponent } from './carrito-modal.component';

import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { AlertService } from '../../../core/services/alert.service';

describe('ModalCarritoComponent', () => {
  let component: ModalCarritoComponent;
  let fixture: ComponentFixture<ModalCarritoComponent>;

  let postService: any;
  let igdbService: any;
  let usuarioService: any;
  let alertService: any;
  let router: any;

  beforeEach(async () => {
    postService = {
      carritoActualizado$: new Subject<void>(),

      obtenerCarrito: jasmine.createSpy().and.returnValue(
        of([
          {
            idApi: 1,
            idPostVenta: 10,
            precio: 20,
            nombreProducto: 'Juego',
          },
        ]),
      ),

      obtenerPrecioCarrito: jasmine.createSpy().and.returnValue(of(20)),

      comprar: jasmine.createSpy().and.returnValue(of(80)),

      eliminarCarrito: jasmine.createSpy().and.returnValue(of(null)),

      vaciarCarrito: jasmine.createSpy().and.returnValue(of(null)),
    };

    igdbService = {
      getCover: jasmine.createSpy().and.returnValue(of('cover.jpg')),
    };

    usuarioService = {
      saldo$: of(100),

      getSaldo: jasmine.createSpy().and.returnValue(100),

      getSaldoFromBackend: jasmine.createSpy().and.returnValue(of(100)),

      setSaldo: jasmine.createSpy(),
    };

    alertService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
    };

    router = {
      navigate: jasmine.createSpy(),
    };

    await TestBed.configureTestingModule({
      imports: [ModalCarritoComponent],
      providers: [
        { provide: PostService, useValue: postService },
        { provide: IgdbService, useValue: igdbService },
        { provide: UsuarioService, useValue: usuarioService },
        { provide: AlertService, useValue: alertService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalCarritoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit carga carrito y precio', () => {
    expect(postService.obtenerCarrito).toHaveBeenCalled();
    expect(postService.obtenerPrecioCarrito).toHaveBeenCalled();
  });

  it('cargarCarrito', () => {
    component.cargarCarrito();

    expect(component.productos.length).toBe(1);
  });

  it('actualizarPrecioTotal', () => {
    component.actualizarPrecioTotal();

    expect(component.precioTotal).toBe(20);
  });

  it('cargarImagen', () => {
    component.cargarImagen(1);

    expect(component.imagenes[1]).toBe('cover.jpg');
  });

  it('cargarImagen no vuelve a cargar si ya existe', () => {
    component.imagenes[1] = 'existente';

    component.cargarImagen(1);

    expect(component.imagenes[1]).toBe('existente');
  });

  it('getImagen devuelve imagen', () => {
    component.imagenes[1] = 'img.jpg';

    expect(component.getImagen(1)).toBe('img.jpg');
  });

  it('getImagen devuelve placeholder', () => {
    expect(component.getImagen(999)).toBe('no-image.png');
  });

  it('tieneSaldo true', () => {
    component.saldo = 100;

    expect(component.tieneSaldo(50)).toBeTrue();
  });

  it('tieneSaldo false', () => {
    component.saldo = 10;

    expect(component.tieneSaldo(50)).toBeFalse();
  });

  it('tieneSaldoTotal true', () => {
    component.saldo = 100;
    component.precioTotal = 50;

    expect(component.tieneSaldoTotal()).toBeTrue();
  });

  it('irASaldo', () => {
    spyOn(component.close, 'emit');

    component.irASaldo();

    expect(component.close.emit).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/saldo']);
  });

  it('comprar redirige si no tiene saldo', () => {
    spyOn(component, 'irASaldo');

    component.saldo = 0;

    component.comprar({
      precio: 100,
      idPostVenta: 1,
    });

    expect(component.irASaldo).toHaveBeenCalled();
  });

  it('comprar ok', () => {
    component.saldo = 100;

    component.productos = [
      {
        idPostVenta: 1,
        precio: 20,
      },
    ];

    component.comprar(component.productos[0]);

    expect(usuarioService.setSaldo).toHaveBeenCalled();
    expect(alertService.success).toHaveBeenCalled();
  });

  it('comprar error', () => {
    postService.comprar.and.returnValue(throwError(() => new Error()));

    component.saldo = 100;

    component.comprar({
      idPostVenta: 1,
      precio: 10,
    });

    expect(alertService.error).toHaveBeenCalled();
  });

  it('eliminar ok', () => {
    component.productos = [
      {
        idPostVenta: 1,
      },
    ];

    component.eliminar(component.productos[0]);

    expect(alertService.success).toHaveBeenCalled();
  });

  it('eliminar error', () => {
    postService.eliminarCarrito.and.returnValue(throwError(() => new Error()));

    component.eliminar({
      idPostVenta: 1,
    });

    expect(alertService.error).toHaveBeenCalled();
  });

  it('comprarTodo sale si no hay productos', () => {
    component.productos = [];

    component.comprarTodo();

    expect(postService.comprar).not.toHaveBeenCalled();
  });

  it('comprarTodo redirige si no tiene saldo', () => {
    spyOn(component, 'irASaldo');

    component.productos = [{ idPostVenta: 1 }];
    component.saldo = 0;
    component.precioTotal = 100;

    component.comprarTodo();

    expect(component.irASaldo).toHaveBeenCalled();
  });

  it('comprarTodo ok', () => {
    spyOn(component.close, 'emit');

    component.productos = [{ idPostVenta: 1 }, { idPostVenta: 2 }];

    component.saldo = 500;
    component.precioTotal = 10;

    component.comprarTodo();

    expect(alertService.success).toHaveBeenCalled();
    expect(component.close.emit).toHaveBeenCalled();
  });

  it('comprarTodo error', () => {
    postService.comprar.and.returnValue(throwError(() => new Error()));

    component.productos = [{ idPostVenta: 1 }];
    component.saldo = 1000;
    component.precioTotal = 10;

    component.comprarTodo();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('vaciarCarrito ok', () => {
    spyOn(component.close, 'emit');

    component.productos = [{ idPostVenta: 1 }];

    component.vaciarCarrito();

    expect(component.productos.length).toBe(0);
    expect(alertService.success).toHaveBeenCalled();
    expect(component.close.emit).toHaveBeenCalled();
  });

  it('vaciarCarrito error', () => {
    postService.vaciarCarrito.and.returnValue(throwError(() => new Error()));

    component.vaciarCarrito();

    expect(alertService.error).toHaveBeenCalled();
  });
});
