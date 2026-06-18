import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewsComponent } from './reviews.component';

import { provideHttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { ReviewService } from '../../../core/services/review.service';
import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { AlertService } from '../../../core/services/alert.service';
import { ConfirmDialogService } from '../../../core/services/confirm-dialog.service';

describe('ReviewsComponent', () => {
  let component: ReviewsComponent;
  let fixture: ComponentFixture<ReviewsComponent>;

  let reviewService: jasmine.SpyObj<ReviewService>;
  let postService: jasmine.SpyObj<PostService>;
  let igdbService: jasmine.SpyObj<IgdbService>;
  let alertService: jasmine.SpyObj<AlertService>;
  let confirmDialogService: jasmine.SpyObj<ConfirmDialogService>;

  beforeEach(async () => {
    reviewService = jasmine.createSpyObj('ReviewService', [
      'getMisReviews',
      'getReviewsEnviadas',
      'eliminarReview',
    ]);

    postService = jasmine.createSpyObj('PostService', [
      'getCompraById',
      'getIntercambioById',
    ]);

    igdbService = jasmine.createSpyObj('IgdbService', ['getCover']);

    alertService = jasmine.createSpyObj('AlertService', ['success', 'error']);

    confirmDialogService = jasmine.createSpyObj('ConfirmDialogService', [
      'confirmar',
    ]);

    reviewService.getMisReviews.and.returnValue(of([]));
    reviewService.getReviewsEnviadas.and.returnValue(of([]));

    igdbService.getCover.and.returnValue(of('cover.jpg'));

    await TestBed.configureTestingModule({
      imports: [ReviewsComponent],
      providers: [
        provideHttpClient(),

        {
          provide: ReviewService,
          useValue: reviewService,
        },
        {
          provide: PostService,
          useValue: postService,
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

    fixture = TestBed.createComponent(ReviewsComponent);
    component = fixture.componentInstance;

    component.nombreUsuario = 'angel';

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ngOnInit', () => {
    component.ngOnInit();

    expect(reviewService.getMisReviews).toHaveBeenCalledWith('angel');

    expect(reviewService.getReviewsEnviadas).toHaveBeenCalled();
  });

  it('cargarReviewsRecibidas', () => {
    const reviews = [{ id: 1 }];

    reviewService.getMisReviews.and.returnValue(of(reviews));

    component.cargarReviewsRecibidas();

    expect(component.reviewsRecibidas).toEqual(reviews);
  });

  it('cargarReviewsEnviadas', () => {
    const reviews = [{ id: 1 }];

    reviewService.getReviewsEnviadas.and.returnValue(of(reviews));

    component.cargarReviewsEnviadas();

    expect(component.reviewsEnviadas).toEqual(reviews);
  });

  it('avatar', () => {
    const result = component.avatar('Angel');

    expect(result).toContain('Angel');
  });

  it('cargarImagen primera vez', () => {
    component.cargarImagen(10);

    expect(igdbService.getCover).toHaveBeenCalledWith(10);

    expect(component.imagenes[10]).toBe('cover.jpg');
  });

  it('cargarImagen ya cargada', () => {
    component.imagenes[10] = 'cached.jpg';

    component.cargarImagen(10);

    expect(igdbService.getCover).not.toHaveBeenCalled();
  });

  it('getImagen existente', () => {
    component.imagenes[5] = 'img.jpg';

    expect(component.getImagen(5)).toBe('img.jpg');
  });

  it('getImagen default', () => {
    expect(component.getImagen(99)).toBe('no-image.png');
  });

  it('eliminarReview cancelado', async () => {
    confirmDialogService.confirmar.and.resolveTo(false);

    await component.eliminarReview(1);

    expect(reviewService.eliminarReview).not.toHaveBeenCalled();
  });

  it('eliminarReview ok', async () => {
    component.reviewsEnviadas = [{ id: 1 }, { id: 2 }];

    confirmDialogService.confirmar.and.resolveTo(true);

    reviewService.eliminarReview.and.returnValue(of(undefined));

    await component.eliminarReview(1);

    expect(component.reviewsEnviadas.length).toBe(1);

    expect(alertService.success).toHaveBeenCalled();
  });

  it('eliminarReview error', async () => {
    confirmDialogService.confirmar.and.resolveTo(true);

    reviewService.eliminarReview.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    await component.eliminarReview(1);

    expect(console.error).toHaveBeenCalled();

    expect(alertService.error).toHaveBeenCalled();
  });

  it('verDetalles venta', () => {
    postService.getCompraById.and.returnValue(
      of({
        idApiProducto: 10,
      }),
    );

    spyOn(component, 'cargarImagen');

    component.verDetalles({
      tipoReview: 'VENTA',
      idCompraVenta: 1,
    });

    expect(postService.getCompraById).toHaveBeenCalledWith(1);

    expect(component.modalDetalleOpen).toBeTrue();

    expect(component.postDetalle.tipo).toBe('VENTA');
  });

  it('verDetalles intercambio', () => {
    postService.getIntercambioById.and.returnValue(
      of({
        idApiProductoOfrecido: 10,
        idApiProductoDeseado: 20,
      }),
    );

    spyOn(component, 'cargarImagen');

    component.verDetalles({
      tipoReview: 'INTERCAMBIO',
      idIntercambio: 1,
    });

    expect(postService.getIntercambioById).toHaveBeenCalledWith(1);

    expect(component.modalDetalleOpen).toBeTrue();

    expect(component.postDetalle.tipo).toBe('INTERCAMBIO');
  });

  it('cerrarDetalle', () => {
    component.modalDetalleOpen = true;
    component.postDetalle = { id: 1 };

    component.cerrarDetalle();

    expect(component.modalDetalleOpen).toBeFalse();

    expect(component.postDetalle).toBeNull();
  });
});
