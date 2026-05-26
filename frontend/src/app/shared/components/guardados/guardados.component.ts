import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { PostService } from '../../../core/services/post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { PostDetalleComponent } from '../modales/modalDetalle/post-detalle.component';

@Component({
  selector: 'app-guardados',
  standalone: true,
  imports: [NgFor, NgIf, PostDetalleComponent],
  templateUrl: './guardados.component.html',
  styleUrls: ['./guardados.component.css'],
})
export class GuardadosComponent implements OnInit {
  publicaciones: any[] = [];
  intercambios: any[] = [];

  imagenes: { [key: number]: string } = {};

  modalOpen = false;
  postSeleccionado: any = null;

  constructor(
    private postService: PostService,
    private igdbService: IgdbService,
  ) {}

  ngOnInit(): void {
    this.cargarPublicaciones();
    this.cargarIntercambios();
  }

  cargarPublicaciones() {
    this.postService.getVentasGuardadas().subscribe((res) => {
      this.publicaciones = res.map((p: any) => ({
        ...p,
        tipo: 'VENTA',
      }));

      this.publicaciones.forEach((p) => {
        this.cargarImagen(p.idApi);
      });
    });
  }

  cargarIntercambios() {
    this.postService.getIntercambiosGuardados().subscribe((res) => {
      this.intercambios = res.map((i: any) => ({
        ...i,
        tipo: 'INTERCAMBIO',
      }));

      this.intercambios.forEach((i) => {
        this.cargarImagen(i.idApiProducto);
        this.cargarImagen(i.idApiProductoCambio);
      });
    });
  }

  cargarImagen(idApi: number) {
    if (this.imagenes[idApi]) return;

    this.igdbService.getCover(idApi).subscribe((url) => {
      this.imagenes[idApi] = url || 'assets/no-image.png';
    });
  }

  getImagen(idApi: number): string {
    return this.imagenes[idApi] || 'assets/no-image.png';
  }

  abrirDetalle(post: any) {
    if (post.tipo === 'VENTA') {
      post.imagen = this.getImagen(post.idApi);
    } else {
      post.imagen = this.getImagen(post.idApiProducto);
      post.imagenIntercambio = this.getImagen(post.idApiProductoCambio);
    }

    this.postSeleccionado = post;
    this.modalOpen = true;
  }

  cerrarModal() {
    this.modalOpen = false;
    this.postSeleccionado = null;
  }

  actualizarGuardados() {
    this.cargarPublicaciones();
    this.cargarIntercambios();
  }

  publicacionesOpen = true;
  intercambiosOpen = true;
}
