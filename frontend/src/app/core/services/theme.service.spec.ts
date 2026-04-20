import { TestBed } from '@angular/core/testing';
import { ThemeService } from './theme.service';

describe('ThemeService', () => {
  let service: ThemeService;
  const THEME_KEY = 'theme';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ThemeService]
    });
    service = TestBed.inject(ThemeService);
    localStorage.clear();
    document.body.className = '';
  });

  afterEach(() => {
    localStorage.clear();
    document.body.className = '';
  });

  it('debe crearse correctamente', () => {
    expect(service).toBeTruthy();
  });

  it('initTheme() debe establecer "light" por defecto si el localStorage está vacío', () => {
    service.initTheme();
    expect(document.body.className).toBe('light');
  });

  it('initTheme() debe establecer el tema desde el localStorage si existe', () => {
    localStorage.setItem(THEME_KEY, 'dark');
    service.initTheme();
    expect(document.body.className).toBe('dark');
  });

  it('toggleTheme() debe cambiar de light a dark', () => {
    document.body.classList.add('light');
    service.toggleTheme();
    expect(document.body.classList.contains('dark')).toBeTrue();
    expect(document.body.classList.contains('light')).toBeFalse();
    expect(localStorage.getItem(THEME_KEY)).toBe('dark');
  });

  it('toggleTheme() debe cambiar de dark a light', () => {
    document.body.classList.add('dark');
    service.toggleTheme();
    expect(document.body.classList.contains('light')).toBeTrue();
    expect(document.body.classList.contains('dark')).toBeFalse();
    expect(localStorage.getItem(THEME_KEY)).toBe('light');
  });

  it('isDark() debe retornar true si el body tiene la clase "dark"', () => {
    document.body.classList.add('dark');
    expect(service.isDark()).toBeTrue();
  });

  it('isDark() debe retornar false si el body no tiene la clase "dark"', () => {
    document.body.classList.add('light');
    expect(service.isDark()).toBeFalse();
  });
});