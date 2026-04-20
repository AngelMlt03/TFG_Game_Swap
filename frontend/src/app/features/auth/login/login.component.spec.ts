import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  
  // Mocks
  const authServiceMock = {
    login: jasmine.createSpy('login').and.returnValue(of({}))
  };
  const routerMock = {
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería llamar a auth.login y navegar al inicio al hacer login', () => {
    component.username = 'testuser';
    component.password = '123456';
    
    component.login();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      username: 'testuser',
      password: '123456'
    });
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('debería navegar a /register al llamar a goRegister', () => {
    component.goRegister();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/register']);
  });
});