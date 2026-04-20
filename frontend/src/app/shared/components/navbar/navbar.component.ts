import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { TokenService } from '../../../core/services/token.service';
import { ThemeService } from '../../../core/services/theme.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgIf],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  username = '';
  menuOpen = false;
  dropdownOpen = false;
  scrolled = false;

  avatarLetter = '';
  avatarColor = '';

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly tokenService: TokenService,
    public theme: ThemeService
  ) {}

  ngOnInit() {
    this.username = this.tokenService.getUsername() || 'Usuario';
  
    this.avatarLetter = this.username.charAt(0).toUpperCase();
    this.avatarColor = this.getColorFromUsername(this.username);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  toggleTheme() {
    this.theme.toggleTheme();
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.scrolled = window.scrollY > 10;
  }

  getColorFromUsername(name: string): string {
    const colors = [
      '#4f46e5',
      '#16a34a',
      '#dc2626',
      '#ea580c',
      '#0891b2',
      '#7c3aed'
    ];

    let hash = 0;
    for (const char of name) {
      const codePoint = char.codePointAt(0) || 0;
      hash = codePoint + ((hash << 5) - hash);
    }

    return colors[Math.abs(hash) % colors.length];
  }

}