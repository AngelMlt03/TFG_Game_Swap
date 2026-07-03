import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';
import { FormsModule } from '@angular/forms';
import { ModalVentaComponent } from '../modales/modalVenta/modal-venta.component';
import { ModalCarritoComponent } from '../carrito/carrito-modal.component';
import { UsuarioService } from '../../../core/services/usuario.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { Usuario } from '../../../core/models/usuario.model';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ModalIntercambioComponent } from '../modales/modalIntercambio/modal-intercambio.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    ModalVentaComponent,
    NgIf,
    NgFor,
    AsyncPipe,
    ModalIntercambioComponent,
    ModalCarritoComponent,
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  menuOpen = false;
  dropdownOpen = false;
  scrolled = false;

  usuario!: Usuario;
  avatarColor = '';
  saldo$;

  search = '';

  sugerencias: any[] = [];
  loading = false;
  focused = false;

  juegoSeleccionado: any = null;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    public theme: ThemeService,
    private usuarioService: UsuarioService,
    private igdbService: IgdbService,
    private elRef: ElementRef,
  ) {
    this.saldo$ = this.usuarioService.saldo$;
  }

  ngOnInit() {
    this.cargarPerfil();
    this.usuarioService.getSaldoFromBackend().subscribe((saldo) => {
      this.usuarioService.setSaldo(saldo);
    });
  }

  cargarPerfil() {
    this.usuarioService.getPerfil().subscribe({
      next: (data) => (this.usuario = data),
      error: (err) => console.error(err),
    });
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

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const clickedInside = this.elRef.nativeElement
      .querySelector('.dropdown')
      ?.contains(event.target);

    if (!clickedInside) {
      this.dropdownOpen = false;
    }
  }

  avatar() {
    return `https:ui-avatars.com/api/?name=${this.usuario.nombreUsuario}&background=random`;
  }

  getColorFromUsername(name: string): string {
    const colors = [
      '#4f46e5',
      '#16a34a',
      '#dc2626',
      '#ea580c',
      '#0891b2',
      '#7c3aed',
    ];

    let hash = 0;
    for (const char of name) {
      const codePoint = char.codePointAt(0) || 0;
      hash = codePoint + ((hash << 5) - hash);
    }

    return colors[Math.abs(hash) % colors.length];
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

  buscarAutocomplete() {
    if (!this.search.trim()) {
      this.sugerencias = [];
      return;
    }

    this.loading = true;

    this.igdbService.buscarJuegos(this.search).subscribe({
      next: (res) => {
        this.sugerencias = res;
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }

  seleccionarJuego(j: any) {
    this.search = j.nombre;
    this.juegoSeleccionado = j;
    this.sugerencias = [];
    this.focused = false;
  }

  buscar() {
    if (!this.search.trim()) return;

    this.router.navigate(['/busqueda'], {
      queryParams: {
        nombre: this.search,
      },
    });

    this.focused = false;
    this.search = '';
  }

  onFocus() {
    this.focused = true;
  }

  onBlur() {
    setTimeout(() => (this.focused = false), 150);
  }
}
