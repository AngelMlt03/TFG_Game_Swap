import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { TokenService } from '../../../core/services/token.service';
import { ThemeService } from '../../../core/services/theme.service';
import { FormsModule } from '@angular/forms';
import { ModalComponent } from '../modal/modal.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, FormsModule, ModalComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  menuOpen = false;
  dropdownOpen = false;
  scrolled = false;

  username = '';
  avatarLetter = '';
  avatarColor = '';
  saldo = 120;

  search = '';

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

  buscar() {
    if (!this.search.trim()) return;

    this.router.navigate(['/busqueda'], {
      queryParams: { nombre: this.search }
    });
  }

  private toggleBodyScroll(isLocked: boolean) {
    if (isLocked) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }
  }

  modalVenta = false;
  modalIntercambio = false;
  modalCarrito = false;

  abrirModalVenta() {
    this.modalVenta = true;
    this.toggleBodyScroll(true);
  }

  abrirModalIntercambio() {
    this.modalIntercambio = true;
    this.toggleBodyScroll(true);
  }

  abrirModalCarrito() {
    this.modalCarrito = true;
    this.toggleBodyScroll(true);
  }

  cerrarModalVenta() {
    this.modalVenta = false;
    this.toggleBodyScroll(false);
  }

  cerrarModalIntercambio() {
    this.modalIntercambio = false;
    this.toggleBodyScroll(false);
  }

  cerrarModalCarrito() {
    this.modalCarrito = false;
    this.toggleBodyScroll(false);
  }

}