import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MainLayoutComponent } from './main-layout.component';
import { RouterTestingModule } from '@angular/router/testing';
import { NavbarComponent } from '../shared/components/navbar/navbar.component';
import { FooterComponent } from '../shared/components/footer/footer.component';
import { AuthService } from '../core/services/auth.service';

describe('MainLayoutComponent', () => {
  let component: MainLayoutComponent;
  let fixture: ComponentFixture<MainLayoutComponent>;

  beforeEach(async () => {
    
    const authServiceMock = { logout: jasmine.createSpy('logout') };

    await TestBed.configureTestingModule({
      imports: [
        MainLayoutComponent, 
        RouterTestingModule,
        NavbarComponent,
        FooterComponent
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MainLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el layout', () => {
    expect(component).toBeTruthy();
  });

  it('debería contener el navbar, el footer y el outlet', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-navbar')).not.toBeNull();
    expect(compiled.querySelector('app-footer')).not.toBeNull();
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });
});