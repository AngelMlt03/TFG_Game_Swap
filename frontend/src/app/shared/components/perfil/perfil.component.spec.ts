import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';

import { PerfilComponent } from './perfil.component';

import { UsuarioService } from '../../../core/services/usuario.service';
import { AlertService } from '../../../core/services/alert.service';

import {
  ActivatedRoute,
  Router,
  convertToParamMap,
  provideRouter,
} from '@angular/router';

import { provideHttpClient } from '@angular/common/http';

import { BehaviorSubject, of, throwError } from 'rxjs';

describe('PerfilComponent', () => {
  let component: PerfilComponent;
  let fixture: ComponentFixture<PerfilComponent>;

  let usuarioService: jasmine.SpyObj<UsuarioService>;
  let alertService: jasmine.SpyObj<AlertService>;
  let router: Router;

  const saldoSubject = new BehaviorSubject<number>(100);

  const usuarioMock = {
    id: 1,
    nombre: 'Angel',
    nombreUsuario: 'angel',
    correo: 'angel@test.com',
    fechaNacimiento: '2000-01-01',
    saldo: 100,
    estrellas: 5,
  } as any;

  beforeEach(async () => {
    usuarioService = jasmine.createSpyObj('UsuarioService', [
      'getPerfil',
      'getPerfilPublico',
      'actualizarPerfil',
      'changePassword',
      'getSaldo',
      'getSaldoFromBackend',
      'setSaldo',
    ]);

    (usuarioService as any).saldo$ = saldoSubject.asObservable();

    usuarioService.getSaldo.and.returnValue(100);
    usuarioService.getSaldoFromBackend.and.returnValue(of(100));
    usuarioService.setSaldo.and.stub();

    alertService = jasmine.createSpyObj('AlertService', ['success', 'error']);

    usuarioService.getPerfil.and.returnValue(of(usuarioMock));

    usuarioService.getPerfilPublico.and.returnValue(
      of({
        ...usuarioMock,
        nombreUsuario: 'publico',
      }),
    );

    usuarioService.actualizarPerfil.and.returnValue(of(usuarioMock));

    usuarioService.changePassword.and.returnValue(of({ success: true }));

    await TestBed.configureTestingModule({
      imports: [PerfilComponent],
      providers: [
        provideHttpClient(),
        provideRouter([]),

        {
          provide: UsuarioService,
          useValue: usuarioService,
        },

        {
          provide: AlertService,
          useValue: alertService,
        },

        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({})),
            queryParams: of({}),
          },
        },
      ],
    }).compileComponents();

    router = TestBed.inject(Router);

    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(PerfilComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('cargarPerfil', () => {
    component.cargarPerfil();

    expect(usuarioService.getPerfil).toHaveBeenCalled();
    expect(component.usuario.nombre).toBe('Angel');
  });

  it('cargarPerfil error', () => {
    usuarioService.getPerfil.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    component.cargarPerfil();

    expect(console.error).toHaveBeenCalled();
  });

  it('cargarPerfilPublico', () => {
    component.cargarPerfilPublico('publico');

    expect(usuarioService.getPerfilPublico).toHaveBeenCalledWith('publico');

    expect(component.usuario.nombreUsuario).toBe('publico');
  });

  it('cargarPerfilPublico error', () => {
    usuarioService.getPerfilPublico.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    component.cargarPerfilPublico('publico');

    expect(console.error).toHaveBeenCalled();
  });

  it('toggleEdit', () => {
    expect(component.editMode).toBeFalse();

    component.toggleEdit();

    expect(component.editMode).toBeTrue();
  });

  it('togglePassword', () => {
    expect(component.editPassword).toBeFalse();

    component.togglePassword();

    expect(component.editPassword).toBeTrue();
  });

  it('guardar', () => {
    component.usuario = { ...usuarioMock };

    component.editMode = true;

    component.guardar();

    expect(usuarioService.actualizarPerfil).toHaveBeenCalled();

    expect(component.editMode).toBeFalse();
  });

  it('guardar error', () => {
    usuarioService.actualizarPerfil.and.returnValue(
      throwError(() => new Error('error')),
    );

    spyOn(console, 'error');

    component.usuario = { ...usuarioMock };

    component.guardar();

    expect(console.error).toHaveBeenCalled();
  });

  it('guardarPassword ok', () => {
    component.currentPassword = 'old';
    component.newPassword = 'new';

    component.guardarPassword();

    expect(usuarioService.changePassword).toHaveBeenCalledWith('old', 'new');

    expect(alertService.success).toHaveBeenCalled();

    expect(component.editPassword).toBeFalse();

    expect(component.currentPassword).toBe('');

    expect(component.newPassword).toBe('');
  });

  it('guardarPassword error', () => {
    usuarioService.changePassword.and.returnValue(
      throwError(() => ({
        error: {
          message: 'Error custom',
        },
      })),
    );

    component.guardarPassword();

    expect(alertService.error).toHaveBeenCalledWith('Error custom');
  });

  it('setTab', fakeAsync(() => {
    spyOn(window, 'scrollTo');

    component.setTab('historial');

    tick();

    expect(component.activeTab).toBe('historial');

    expect(router.navigate).toHaveBeenCalled();
  }));

  it('scrollToTabs', fakeAsync(() => {
    const scrollSpy = jasmine.createSpy();

    component.tabsSection = {
      nativeElement: {
        scrollIntoView: scrollSpy,
      },
    } as any;

    component.scrollToTabs();

    tick(300);

    expect(scrollSpy).toHaveBeenCalled();
  }));

  it('ngOnInit perfil publico', () => {
    const route = TestBed.inject(ActivatedRoute) as any;

    route.paramMap = of(
      convertToParamMap({
        nombreUsuario: 'publico',
      }),
    );

    component.ngOnInit();

    expect(component.esPerfilPublico).toBeTrue();
  });

  it('ngOnInit carga perfil privado', () => {
    const route = TestBed.inject(ActivatedRoute) as any;

    route.paramMap = of(convertToParamMap({}));

    component.ngOnInit();

    expect(usuarioService.getPerfil).toHaveBeenCalled();
  });

  it('ngOnInit con tab en query params', () => {
    spyOn(component, 'scrollToTabs');

    const route = TestBed.inject(ActivatedRoute) as any;

    route.queryParams = of({
      tab: 'reviews',
    });

    component.ngOnInit();

    expect(component.activeTab).toBe('reviews');

    expect(component.scrollToTabs).toHaveBeenCalled();
  });
});
