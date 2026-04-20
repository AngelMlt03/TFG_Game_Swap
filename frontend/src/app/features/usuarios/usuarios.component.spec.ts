import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UsuariosComponent } from './usuarios.component';
import { UsuarioService } from '../../core/services/usuario.service';
import { of } from 'rxjs';
import { Usuario } from '../../core/models/usuario.model';

describe('UsuariosComponent', () => {
  let component: UsuariosComponent;
  let fixture: ComponentFixture<UsuariosComponent>;
  let usuarioServiceMock: any;

  const mockUsuarios: Usuario[] = [
    { nombre: 'Usuario 1', email: 'u1@test.com' },
    { nombre: 'Usuario 2', email: 'u2@test.com' }
  ];

  beforeEach(async () => {

    usuarioServiceMock = {
      getUsuarios: jasmine.createSpy('getUsuarios').and.returnValue(of(mockUsuarios)),
      crearUsuario: jasmine.createSpy('crearUsuario').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [UsuariosComponent], 
      providers: [
        { provide: UsuarioService, useValue: usuarioServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UsuariosComponent);
    component = fixture.componentInstance;
  });

  it('debe crearse el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debe cargar usuarios al inicializar (ngOnInit)', () => {
    fixture.detectChanges(); 

    expect(usuarioServiceMock.getUsuarios).toHaveBeenCalled();
    expect(component.usuarios.length).toBe(2);
    expect(component.usuarios).toEqual(mockUsuarios);
  });

  it('debe llamar a cargarUsuarios y actualizar la lista', () => {
    component.cargarUsuarios();
    
    expect(usuarioServiceMock.getUsuarios).toHaveBeenCalled();
    expect(component.usuarios).toEqual(mockUsuarios);
  });

  it('debe crear un usuario y volver a cargar la lista', () => {
    
    fixture.detectChanges(); 

    usuarioServiceMock.getUsuarios.calls.reset();

    component.crearUsuario();

    expect(usuarioServiceMock.crearUsuario).toHaveBeenCalledWith(jasmine.objectContaining({
      nombre: 'Angular Test'
    }));

    expect(usuarioServiceMock.getUsuarios).toHaveBeenCalled();
  });
});