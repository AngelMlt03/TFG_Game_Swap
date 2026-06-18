import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { ModalReviewComponent } from './modal-review.component';

import { ReviewService } from '../../../../core/services/review.service';
import { UsuarioService } from '../../../../core/services/usuario.service';
import { AlertService } from '../../../../core/services/alert.service';

describe('ModalReviewComponent', () => {
  let component: ModalReviewComponent;
  let fixture: ComponentFixture<ModalReviewComponent>;

  let reviewService: any;
  let usuarioService: any;
  let alertService: any;

  beforeEach(async () => {
    reviewService = {
      crearReview: jasmine.createSpy().and.returnValue(of({})),
    };

    usuarioService = {
      getPerfil: jasmine.createSpy().and.returnValue(
        of({
          id: 1,
          nombreUsuario: 'usuario'
        })
      ),
    };

    alertService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
      info: jasmine.createSpy(),
    };

    await TestBed.configureTestingModule({
      imports: [ModalReviewComponent],
      providers: [
        { provide: ReviewService, useValue: reviewService },
        { provide: UsuarioService, useValue: usuarioService },
        { provide: AlertService, useValue: alertService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit carga usuario', () => {
    component.ngOnInit();

    expect(usuarioService.getPerfil).toHaveBeenCalled();
    expect(component.usuarioActual.id).toBe(1);
  });

  it('enviar muestra aviso si contenido vacío', () => {
    component.contenido = '   ';
    component.estrellas = 5;

    component.enviar();

    expect(alertService.info).toHaveBeenCalled();
    expect(reviewService.crearReview).not.toHaveBeenCalled();
  });

  it('enviar muestra aviso si estrellas <= 0', () => {
    component.contenido = 'Review';
    component.estrellas = 0;

    component.enviar();

    expect(alertService.info).toHaveBeenCalled();
    expect(reviewService.crearReview).not.toHaveBeenCalled();
  });

  it('enviar review venta correctamente', () => {
    spyOn(component.close, 'emit');

    component.tipo = 'VENTA';

    component.post = {
      id: 10,
      idVendedor: 20,
      nombreVendedor: 'Pepe'
    };

    component.contenido = 'Muy bien';
    component.estrellas = 5;

    component.enviar();

    expect(reviewService.crearReview).toHaveBeenCalled();

    expect(alertService.success).toHaveBeenCalledWith(
      'Review enviada'
    );

    expect(component.close.emit).toHaveBeenCalled();
    expect(component.loading).toBeFalse();
  });

  it('enviar review intercambio cuando usuario es publicador', () => {
    component.tipo = 'INTERCAMBIO';

    component.usuarioActual = {
      id: 1
    };

    component.post = {
      id: 10,
      idUsuarioPublicador: 1,
      idUsuarioCambio: 2,
      nombreUsuarioCambio: 'Luis',
      nombreUsuarioPublicador: 'Pedro'
    };

    component.contenido = 'Review';
    component.estrellas = 4;

    component.enviar();

    const dto =
      reviewService.crearReview.calls.mostRecent().args[0];

    expect(dto.idReviewed).toBe(2);
  });

  it('enviar review intercambio cuando usuario es cambio', () => {
    component.tipo = 'INTERCAMBIO';

    component.usuarioActual = {
      id: 99
    };

    component.post = {
      id: 10,
      idUsuarioPublicador: 1,
      idUsuarioCambio: 2,
      nombreUsuarioCambio: 'Luis',
      nombreUsuarioPublicador: 'Pedro'
    };

    component.contenido = 'Review';
    component.estrellas = 4;

    component.enviar();

    const dto =
      reviewService.crearReview.calls.mostRecent().args[0];

    expect(dto.idReviewed).toBe(1);
  });

  it('enviar controla error backend', () => {
    reviewService.crearReview.and.returnValue(
      throwError(() => new Error('error'))
    );

    component.tipo = 'VENTA';

    component.post = {
      id: 10,
      idVendedor: 20,
      nombreVendedor: 'Pepe'
    };

    component.contenido = 'Review';
    component.estrellas = 5;

    component.enviar();

    expect(alertService.error).toHaveBeenCalledWith(
      'Error al enviar la review'
    );

    expect(component.loading).toBeFalse();
  });

  it('getNombreUsuario devuelve vendedor en venta', () => {
    component.tipo = 'VENTA';

    component.post = {
      nombreVendedor: 'Pepe'
    };

    expect(component.getNombreUsuario())
      .toBe('Pepe');
  });

  it('getNombreUsuario devuelve usuario cambio', () => {
    component.tipo = 'INTERCAMBIO';

    component.usuarioActual = {
      id: 1
    };

    component.post = {
      idUsuarioPublicador: 1,
      nombreUsuarioCambio: 'Luis',
      nombreUsuarioPublicador: 'Pedro'
    };

    expect(component.getNombreUsuario())
      .toBe('Luis');
  });

  it('getNombreUsuario devuelve usuario publicador', () => {
    component.tipo = 'INTERCAMBIO';

    component.usuarioActual = {
      id: 99
    };

    component.post = {
      idUsuarioPublicador: 1,
      nombreUsuarioCambio: 'Luis',
      nombreUsuarioPublicador: 'Pedro'
    };

    expect(component.getNombreUsuario())
      .toBe('Pedro');
  });

  it('estrellasArray tiene valores esperados', () => {
    expect(component.estrellasArray)
      .toEqual([0, 1, 2, 3, 4, 5]);
  });

  it('abre y cierra secciones', () => {
    component.usuarioOpen = true;
    component.contenidoOpen = true;
    component.puntuacionOpen = true;

    component.usuarioOpen = !component.usuarioOpen;
    component.contenidoOpen = !component.contenidoOpen;
    component.puntuacionOpen = !component.puntuacionOpen;

    expect(component.usuarioOpen).toBeFalse();
    expect(component.contenidoOpen).toBeFalse();
    expect(component.puntuacionOpen).toBeFalse();
  });
});