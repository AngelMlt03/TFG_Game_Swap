import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [FormsModule, NgIf, RouterLink],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {

  usuario = {
    nombre: 'Angel',
    email: 'angel@gmail.com'
  };

  editMode = false;

  toggleEdit() {
    this.editMode = !this.editMode;
  }

  guardar() {
    console.log('Guardar usuario:', this.usuario);
    this.editMode = false;
  }
}