import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../models/usuario.model';

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
      email: 'angular@test.com'
    };

    this.usuarioService.crearUsuario(nuevoUsuario).subscribe(() => {
      this.cargarUsuarios();
    });
  }
}
