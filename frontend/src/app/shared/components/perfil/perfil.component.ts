import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ActivatedRoute, Router } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';
import { MisPublicacionesComponent } from '../misPublicaciones/mis-publicaciones.component';
import { HistorialComponent } from '../historial/historial.component';
import { GuardadosComponent } from '../guardados/guardados.component';
import { ReviewsComponent } from '../reviews/reviews.component';
import { ViewChild, ElementRef } from '@angular/core';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgClass,
    RouterLink,
    MisPublicacionesComponent,
    HistorialComponent,
    GuardadosComponent,
    ReviewsComponent,
  ],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css'],
})
export class PerfilComponent implements OnInit {
  @ViewChild('tabsSection') tabsSection!: ElementRef;

  usuario!: Usuario;

  editMode = false;
  editPassword = false;

  nuevaPassword = '';

  esPerfilPublico = false;
  nombreUsuarioPerfil = '';

  constructor(
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService,
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const nombreUsuario = params.get('nombreUsuario');

      if (nombreUsuario) {
        this.esPerfilPublico = true;
        this.nombreUsuarioPerfil = nombreUsuario;
        this.cargarPerfilPublico(nombreUsuario);
        this.activeTab = 'misPublicaciones';
      } else {
        this.esPerfilPublico = false;
        this.cargarPerfil();
      }
    });
    this.route.queryParams.subscribe((params) => {
      const tab = params['tab'];
      if (tab) {
        this.activeTab = tab;
        this.scrollToTabs();
      }
    });
  }

  scrollToTabs() {
    setTimeout(() => {
      this.tabsSection?.nativeElement?.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      });
    }, 300);
  }

  cargarPerfil() {
    this.usuarioService.getPerfil().subscribe({
      next: (data) => (this.usuario = data),
      error: (err) => console.error(err),
    });
  }

  cargarPerfilPublico(nombreUsuario: string) {
    this.usuarioService.getPerfilPublico(nombreUsuario).subscribe({
      next: (data) => {
        this.usuario = data;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  toggleEdit() {
    this.editMode = !this.editMode;
  }

  guardar() {
    this.usuarioService.actualizarPerfil(this.usuario).subscribe({
      next: () => {
        this.editMode = false;
      },
      error: (err) => console.error(err),
    });
  }

  togglePassword() {
    this.editPassword = !this.editPassword;
  }

  currentPassword = '';
  newPassword = '';

  guardarPassword() {
    this.usuarioService
      .changePassword(this.currentPassword, this.newPassword)
      .subscribe({
        next: () => {
          this.alertService.success('Contraseña cambiada correctamente');
          this.editPassword = false;
          this.currentPassword = '';
          this.newPassword = '';
        },
        error: (err) => {
          this.alertService.error(
            err.error?.message || 'Error al cambiar contraseña',
          );
        },
      });
  }

  activeTab = 'guardados';

  setTab(tab: string) {
    this.activeTab = tab;

    const currentY = window.scrollY;

    this.router
      .navigate([], {
        relativeTo: this.route,
        queryParams: { tab },
        queryParamsHandling: 'merge',
      })
      .then(() => {
        window.scrollTo({
          top: currentY,
        });
      });
  }
}
