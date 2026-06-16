/* sonar-ignore */
/* istanbul ignore file */
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {

  private readonly THEME_KEY = 'theme';

  initTheme() {
    const theme = localStorage.getItem(this.THEME_KEY) || 'dark';
    document.body.className = theme;
  }

  toggleTheme() {
    const current = document.body.classList.contains('dark') ? 'dark' : 'light';
    const newTheme = current === 'dark' ? 'light' : 'dark';

    document.body.classList.remove('light', 'dark');
    document.body.classList.add(newTheme);
    localStorage.setItem(this.THEME_KEY, newTheme);
  }

  isDark() {
    return document.body.classList.contains('dark');
  }
}