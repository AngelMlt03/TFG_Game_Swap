import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit {

  usuarios: Usuario[] = [];

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.getUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  crearUsuario() {
    const nuevoUsuario: Usuario = {
      nombre: 'Angular Test',
      correo: 'angular@test.com',
      nombreUsuario: 'angular_test',
      fechaNacimiento: '1990-01-01',
      saldo: 100,
      estrellas: 0
    };

    this.usuarioService.crearUsuario(nuevoUsuario).subscribe(() => {
      this.cargarUsuarios();
    });
  }
}
