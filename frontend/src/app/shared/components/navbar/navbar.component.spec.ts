import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { AuthService } from '../../../core/services/auth.service';
import { TokenService } from '../../../core/services/token.service';
import { ThemeService } from '../../../core/services/theme.service';
import { Router, provideRouter } from '@angular/router';
import { By } from '@angular/platform-browser';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  
  const authServiceMock = {
    logout: jasmine.createSpy('logout')
  };

  const tokenServiceMock = {
    getUsername: jasmine.createSpy('getUsername').and.returnValue('Alex')
  };

  const themeServiceMock = {
    toggleTheme: jasmine.createSpy('toggleTheme'),
    isDark: jasmine.createSpy('isDark').and.returnValue(false)
  };

  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      
      imports: [NavbarComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock },
        { provide: TokenService, useValue: tokenServiceMock },
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    
    spyOn(router, 'navigate'); 

    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería alternar dropdownOpen al llamar a toggleDropdown()', () => {
    expect(component.dropdownOpen).toBeFalse();
    component.toggleDropdown();
    expect(component.dropdownOpen).toBeTrue();
    component.toggleDropdown();
    expect(component.dropdownOpen).toBeFalse();
  });

  it('debería llamar a theme.toggleTheme() al ejecutar toggleTheme()', () => {
    component.toggleTheme();
    expect(themeServiceMock.toggleTheme).toHaveBeenCalled();
  });

  it('debería cerrar sesión y navegar al login', () => {
    component.logout();
    expect(authServiceMock.logout).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('debería detectar el scroll de la ventana', () => {
    
    spyOnProperty(window, 'scrollY', 'get').and.returnValue(50);
    window.dispatchEvent(new Event('scroll'));
    fixture.detectChanges();
    
    expect(component.scrolled).toBeTrue();
  });

  it('debería mostrar el menú desplegable solo cuando dropdownOpen es true', () => {
    
    let menu = fixture.debugElement.query(By.css('.dropdown-menu'));
    expect(menu).toBeNull();

    component.dropdownOpen = true;
    fixture.detectChanges();
    
    menu = fixture.debugElement.query(By.css('.dropdown-menu'));
    expect(menu).not.toBeNull();
  });

  it('debería generar una letra de avatar válida incluso si el nombre es minúscula', () => {
    tokenServiceMock.getUsername.and.returnValue('paco');
    component.ngOnInit();
    expect(component.avatarLetter).toBe('P');
  });
}); 