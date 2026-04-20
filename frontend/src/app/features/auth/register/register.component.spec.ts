import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const authServiceMock = {
    register: jasmine.createSpy('register').and.returnValue(of({}))
  };
  const routerMock = {
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería llamar a auth.register con los datos correctos y navegar', () => {
    component.name = 'Juan';
    component.username = 'juanito';
    component.email = 'juan@test.com';
    component.password = 'password123';

    component.register();

    expect(authServiceMock.register).toHaveBeenCalledWith({
      name: 'Juan',
      username: 'juanito',
      correo: 'juan@test.com',
      password: 'password123'
    });
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('debería navegar a login al llamar a goLogin', () => {
    component.goLogin();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });
});