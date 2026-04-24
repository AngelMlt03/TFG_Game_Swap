import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';

@Component({
  standalone: true,
  imports: [FormsModule, NgIf, NgClass, RouterLink],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  usuario!: Usuario;

  editMode = false;
  editPassword = false;

  nuevaPassword = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  cargarPerfil() {
    this.usuarioService.getPerfil().subscribe({
      next: (data) => this.usuario = data,
      error: (err) => console.error(err)
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
      error: (err) => console.error(err)
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
          alert('Contraseña cambiada correctamente');
          this.editPassword = false;
          this.currentPassword = '';
          this.newPassword = '';
        },
        error: (err) => {
          alert(err.error?.message || 'Error al cambiar contraseña');
        }
      });
  }
}