import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { BusquedaComponent } from './busqueda.component';

import { ActivatedRoute } from '@angular/router';
import { IgdbService } from '../../../core/services/igdb.service';
import { PostService } from '../../../core/services/post.service';
import { provideHttpClient } from '@angular/common/http';

describe('BusquedaComponent', () => {
  let component: BusquedaComponent;
  let fixture: ComponentFixture<BusquedaComponent>;

  let igdbService: any;
  let postService: any;

  beforeEach(async () => {
    igdbService = {
      buscarJuegos: jasmine.createSpy().and.returnValue(of([])),
      buscarPlataformas: jasmine.createSpy().and.returnValue(of([])),
      getCover: jasmine.createSpy().and.returnValue(of('cover.png')),
    };

    postService = {
      buscarPosts: jasmine.createSpy().and.returnValue(of([])),
    };

    await TestBed.configureTestingModule({
      imports: [BusquedaComponent],
      providers: [
        provideHttpClient(),

        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: of({}),
          },
        },
        {
          provide: IgdbService,
          useValue: igdbService,
        },
        {
          provide: PostService,
          useValue: postService,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(BusquedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit llama buscar', () => {
    spyOn(component, 'buscar');

    component.ngOnInit();

    expect(component.buscar).toHaveBeenCalled();
  });

  it('buscarJuego sin texto', () => {
    component.filtros.videojuego = '';

    component.buscarJuego();

    expect(igdbService.buscarJuegos).not.toHaveBeenCalled();
  });

  it('buscarJuego éxito', () => {
    igdbService.buscarJuegos.and.returnValue(of([{ nombre: 'Zelda' }]));

    component.filtros.videojuego = 'Zelda';

    component.buscarJuego();

    expect(component.loadingJuego).toBeFalse();
    expect(component.sugerenciasJuego.length).toBe(1);
  });

  it('buscarJuego error', () => {
    igdbService.buscarJuegos.and.returnValue(throwError(() => new Error()));

    component.filtros.videojuego = 'Zelda';

    component.buscarJuego();

    expect(component.loadingJuego).toBeFalse();
  });

  it('buscarPlataforma', () => {
    igdbService.buscarPlataformas.and.returnValue(of([{ nombre: 'PS5' }]));

    component.buscarPlataforma();

    expect(component.sugerenciasPlataforma.length).toBe(1);
  });

  it('buscarTipos', () => {
    component.buscarTipos();

    expect(component.sugerenciasTipo.length).toBeGreaterThan(0);
  });

  it('buscarEstados', () => {
    component.buscarEstados();

    expect(component.sugerenciasEstado.length).toBeGreaterThan(0);
  });

  it('seleccionarJuego', () => {
    component.seleccionarJuego({
      nombre: 'Mario',
    });

    expect(component.filtros.videojuego).toBe('Mario');
    expect(component.focusedJuego).toBeFalse();
  });

  it('seleccionarPlataforma', () => {
    component.seleccionarPlataforma({
      nombre: 'Switch',
    });

    expect(component.filtros.plataforma).toBe('Switch');
    expect(component.focusedPlataforma).toBeFalse();
  });

  it('seleccionarTipo', () => {
    component.seleccionarTipo('VENTA');

    expect(component.filtros.tipo).toBe('VENTA');
  });

  it('seleccionarEstado', () => {
    component.seleccionarEstado('USADO');

    expect(component.filtros.estado).toBe('USADO');
  });

  it('buscar con resultados', () => {
    spyOn(component, 'cargarImagen');

    postService.buscarPosts.and.returnValue(
      of([
        {
          idApi: 1,
        },
      ]),
    );

    component.buscar();

    expect(component.resultados.length).toBe(1);
    expect(component.noResults).toBeFalse();
    expect(component.cargarImagen).toHaveBeenCalled();
  });

  it('buscar sin resultados', () => {
    postService.buscarPosts.and.returnValue(of([]));

    component.buscar();

    expect(component.noResults).toBeTrue();
  });

  it('buscar error', () => {
    postService.buscarPosts.and.returnValue(throwError(() => new Error()));

    component.buscar();

    expect(component.loading).toBeFalse();
  });

  it('getTipoLabel', () => {
    expect(component.getTipoLabel('VENTA')).toBe('Venta');
  });

  it('getEstadoLabel', () => {
    expect(component.getEstadoLabel('USADO')).toBe('Usado');
  });

  it('abrirDetalle venta', () => {
    component.imagenes[1] = 'img.png';

    component.abrirDetalle({
      idApi: 1,
    });

    expect(component.modalOpen).toBeTrue();
  });

  it('abrirDetalle intercambio con imagen cacheada', () => {
    component.imagenes[1] = 'img1';
    component.imagenes[2] = 'img2';

    component.abrirDetalle({
      idApi: 1,
      idApiIntercambio: 2,
    });

    expect(component.modalOpen).toBeTrue();
  });

  it('abrirDetalle intercambio cargando imagen', () => {
    igdbService.getCover.and.returnValue(of('img2'));

    component.imagenes[1] = 'img1';

    component.abrirDetalle({
      idApi: 1,
      idApiIntercambio: 2,
    });

    expect(component.modalOpen).toBeTrue();
  });

  it('cerrarModal', () => {
    component.modalOpen = true;

    component.cerrarModal();

    expect(component.modalOpen).toBeFalse();
  });

  it('getImagen existente', () => {
    component.imagenes[1] = 'img.png';

    expect(component.getImagen(1)).toBe('img.png');
  });

  it('getImagen inexistente', () => {
    expect(component.getImagen(999)).toBe('no-image.png');
  });

  it('cargarImagen éxito', () => {
    component.cargarImagen(1);

    expect(component.imagenes[1]).toBe('cover.png');
  });

  it('cargarImagen error', () => {
    igdbService.getCover.and.returnValue(throwError(() => new Error()));

    component.cargarImagen(2);

    expect(component.imagenes[2]).toBe('no-image.png');
  });

  it('cargarImagen ya cargada', () => {
    component.imagenes[1] = 'img';

    component.cargarImagen(1);

    expect(igdbService.getCover).not.toHaveBeenCalledWith(1);
  });

  it('onAccionRealizada', () => {
    spyOn(component, 'buscar');

    component.onAccionRealizada();

    expect(component.buscar).toHaveBeenCalled();
  });

  it('focus juego', () => {
    component.onFocusJuego();

    expect(component.focusedJuego).toBeTrue();
  });

  it('focus plataforma', () => {
    component.onFocusPlataforma();

    expect(component.focusedPlataforma).toBeTrue();
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

  it('blur tipo', fakeAsync(() => {
    component.focusedTipo = true;

    component.onBlurTipo();

    tick(150);

    expect(component.focusedTipo).toBeFalse();
  }));

  it('blur estado', fakeAsync(() => {
    component.focusedEstado = true;

    component.onBlurEstado();

    tick(150);

    expect(component.focusedEstado).toBeFalse();
  }));
});
