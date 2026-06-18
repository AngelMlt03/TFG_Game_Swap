import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MisPublicacionesComponent } from './mis-publicaciones.component';

import { provideHttpClient } from '@angular/common/http';

import { of, throwError } from 'rxjs';

import { PostService } from '../../../core/services/post.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { AlertService } from '../../../core/services/alert.service';
import { ConfirmDialogService } from '../../../core/services/confirm-dialog.service';

describe('MisPublicacionesComponent', () => {
  let component: MisPublicacionesComponent;
  let fixture: ComponentFixture<MisPublicacionesComponent>;

  let postService: jasmine.SpyObj<PostService>;
  let usuarioService: jasmine.SpyObj<UsuarioService>;
  let igdbService: jasmine.SpyObj<IgdbService>;
  let alertService: jasmine.SpyObj<AlertService>;
  let confirmDialogService: jasmine.SpyObj<ConfirmDialogService>;

  beforeEach(async () => {
    postService = jasmine.createSpyObj('PostService', [
      'eliminarVenta',
      'eliminarIntercambio',
    ]);

    usuarioService = jasmine.createSpyObj('UsuarioService', [
      'getVentas',
      'getIntercambios',
    ]);

    igdbService = jasmine.createSpyObj('IgdbService', ['getCover']);

    alertService = jasmine.createSpyObj('AlertService', ['success', 'error']);

    confirmDialogService = jasmine.createSpyObj('ConfirmDialogService', [
      'confirmar',
    ]);

    usuarioService.getVentas.and.returnValue(
      of([
        {
          id: 1,
          idApi: 10,
          nombreProducto: 'Zelda',
        },
      ]),
    );

    usuarioService.getIntercambios.and.returnValue(
      of([
        {
          id: 2,
          idApi: 20,
          idApiIntercambio: 30,
        },
      ]),
    );

    igdbService.getCover.and.returnValue(of('imagen.jpg'));

    postService.eliminarVenta.and.returnValue(of({}));
    postService.eliminarIntercambio.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [MisPublicacionesComponent],
      providers: [
        provideHttpClient(),

        {
          provide: PostService,
          useValue: postService,
        },
        {
          provide: UsuarioService,
          useValue: usuarioService,
        },
        {
          provide: IgdbService,
          useValue: igdbService,
        },
        {
          provide: AlertService,
          useValue: alertService,
        },
        {
          provide: ConfirmDialogService,
          useValue: confirmDialogService,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MisPublicacionesComponent);
    component = fixture.componentInstance;

    component.nombreUsuario = 'angel';

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit carga ventas e intercambios', () => {
    expect(usuarioService.getVentas).toHaveBeenCalled();
    expect(usuarioService.getIntercambios).toHaveBeenCalled();
  });

  it('cargarVentas', () => {
    component.cargarVentas();

    expect(component.ventas.length).toBe(1);
  });

  it('cargarIntercambios', () => {
    component.cargarIntercambios();

    expect(component.intercambios.length).toBe(1);
  });

  it('cargarImagen', () => {
    component.cargarImagen(10);

    expect(igdbService.getCover).toHaveBeenCalledWith(10);
    expect(component.imagenes[10]).toBe('imagen.jpg');
  });

  it('getImagen devuelve imagen', () => {
    component.imagenes[1] = 'test.jpg';

    expect(component.getImagen(1)).toBe('test.jpg');
  });

  it('getImagen devuelve fallback', () => {
    expect(component.getImagen(999)).toBe('no-image.png');
  });

  it('editarVenta', () => {
    component.imagenes[10] = 'img.jpg';

    const post = {
      idApi: 10,
    };

    component.editarVenta(post);

    expect(component.modalEditarOpen).toBeTrue();
    expect(component.postEditar).toBe(post);
  });

  it('editarIntercambio', () => {
    component.imagenes[20] = 'a.jpg';
    component.imagenes[30] = 'b.jpg';

    const post = {
      idApi: 20,
      idApiIntercambio: 30,
    };

    component.editarIntercambio(post);

    expect(component.modalEditarIntercambioOpen).toBeTrue();
  });

  it('eliminarVenta confirmado', async () => {
    component.ventas = [{ id: 1 }];

    confirmDialogService.confirmar.and.resolveTo(true);

    await component.eliminarVenta(1);

    expect(postService.eliminarVenta).toHaveBeenCalledWith(1);
  });

  it('eliminarVenta cancelado', async () => {
    confirmDialogService.confirmar.and.resolveTo(false);

    await component.eliminarVenta(1);

    expect(postService.eliminarVenta).not.toHaveBeenCalled();
  });

  it('eliminarVenta error', async () => {
    confirmDialogService.confirmar.and.resolveTo(true);

    postService.eliminarVenta.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    await component.eliminarVenta(1);

    expect(console.error).toHaveBeenCalled();
    expect(alertService.error).toHaveBeenCalled();
  });

  it('eliminarIntercambio confirmado', async () => {
    component.intercambios = [{ id: 2 }];

    confirmDialogService.confirmar.and.resolveTo(true);

    await component.eliminarIntercambio(2);

    expect(postService.eliminarIntercambio).toHaveBeenCalledWith(2);
  });

  it('eliminarIntercambio cancelado', async () => {
    confirmDialogService.confirmar.and.resolveTo(false);

    await component.eliminarIntercambio(2);

    expect(postService.eliminarIntercambio).not.toHaveBeenCalled();
  });

  it('eliminarIntercambio error', async () => {
    confirmDialogService.confirmar.and.resolveTo(true);

    postService.eliminarIntercambio.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    await component.eliminarIntercambio(2);

    expect(console.error).toHaveBeenCalled();
    expect(alertService.error).toHaveBeenCalled();
  });

  it('cerrarModalEditar sin actualizar', () => {
    component.modalEditarOpen = true;
    component.postEditar = {};

    component.cerrarModalEditar(false);

    expect(component.modalEditarOpen).toBeFalse();
    expect(component.postEditar).toBeNull();
  });

  it('cerrarModalEditar con actualizar', () => {
    spyOn(component, 'cargarVentas');
    spyOn(component, 'cargarIntercambios');

    component.cerrarModalEditar(true);

    expect(component.cargarVentas).toHaveBeenCalled();
    expect(component.cargarIntercambios).toHaveBeenCalled();
  });

  it('cerrarModalEditarIntercambio con actualizar', () => {
    spyOn(component, 'cargarVentas');
    spyOn(component, 'cargarIntercambios');

    component.cerrarModalEditarIntercambio(true);

    expect(component.cargarVentas).toHaveBeenCalled();
    expect(component.cargarIntercambios).toHaveBeenCalled();
  });

  it('abrirConversionIntercambio', () => {
    component.abrirConversionIntercambio({
      idVenta: 1,
      idApi: 10,
      nombreProducto: 'Juego',
      plataforma: 'PS5',
      estado: 'NUEVO',
      descripcion: 'desc',
      imagen: 'img',
    });

    expect(component.modalConversionIntercambioOpen).toBeTrue();
    expect(component.modalEditarOpen).toBeFalse();
  });

  it('abrirConversionVenta', () => {
    component.abrirConversionVenta({
      idIntercambio: 2,
      idApi: 10,
      nombreProducto: 'Juego',
      plataforma: 'PS5',
      estado: 'NUEVO',
      descripcion: 'desc',
      imagen: 'img',
    });

    expect(component.modalConversionVentaOpen).toBeTrue();
    expect(component.modalEditarIntercambioOpen).toBeFalse();
  });

  it('cerrarModalConversionVenta con actualizar', () => {
    spyOn(component, 'cargarVentas');
    spyOn(component, 'cargarIntercambios');

    component.cerrarModalConversionVenta(true);

    expect(component.cargarVentas).toHaveBeenCalled();
    expect(component.cargarIntercambios).toHaveBeenCalled();
  });

  it('cerrarModalConversionIntercambio con actualizar', () => {
    spyOn(component, 'cargarVentas');
    spyOn(component, 'cargarIntercambios');

    component.cerrarModalConversionIntercambio(true);

    expect(component.cargarVentas).toHaveBeenCalled();
    expect(component.cargarIntercambios).toHaveBeenCalled();
  });
});
