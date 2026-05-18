import { Component, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { PostService } from '../../../core/services//post.service';
import { IgdbService } from '../../../core/services/igdb.service';
import { ModalHistorialDetalleComponent } from './modal-historial-detalle.component';

@Component({
  selector: 'app-historial',
  standalone: true,
  imports: [NgFor, NgIf, ModalHistorialDetalleComponent],
  templateUrl: './historial.component.html',
  styleUrls: ['../misPublicaciones/mis-publicaciones.component.css']
})
export class HistorialComponent implements OnInit {

  compras: any[] = [];
  ventas: any[] = [];
  intercambios: any[] = [];

  imagenes: { [key: number]: string } = {};

  modalDetalleOpen = false;
  postDetalle: any = null;

  constructor(
    private postService: PostService,
    private igdbService: IgdbService
  ) {}

  ngOnInit() {
    this.cargarCompras();
    this.cargarVentas();
    this.cargarIntercambios();
  }

  cargarCompras() {

    this.postService
      .getHistorialCompras()
      .subscribe(res => {

        this.compras = res;

        res.forEach((c: any) =>
          this.cargarImagen(c.idApiProducto)
        );
      });
  }

  cargarVentas() {

    this.postService
      .getHistorialVentas()
      .subscribe(res => {

        this.ventas = res;

        res.forEach((v: any) =>
          this.cargarImagen(v.idApiProducto)
        );
      });
  }

  cargarIntercambios() {

    this.postService
      .getHistorialIntercambios()
      .subscribe(res => {

        this.intercambios = res;

        res.forEach((i: any) => {

          this.cargarImagen(i.idApiProductoOfrecido);
          this.cargarImagen(i.idApiProductoDeseado);
        });
      });
  }

  cargarImagen(idApi: number) {
    if (this.imagenes[idApi]) return;

    this.igdbService.getCover(idApi).subscribe(url => {
      this.imagenes[idApi] = url || 'assets/no-image.png';
    });
  }

  getImagen(idApi: number) {
    return this.imagenes[idApi] || 'assets/no-image.png';
  }

  verDetalle(post: any, tipo: string) {

    post.tipo = tipo;

    if (tipo === 'VENTA') {

      post.imagen = this.getImagen(post.idApiProducto);

    } else {

      post.imagen = this.getImagen(post.idApiProductoOfrecido);
      post.imagenIntercambio = this.getImagen(post.idApiProductoDeseado);
    }

    this.postDetalle = post;
    this.modalDetalleOpen = true;
  }

  cerrarDetalle() {

    this.modalDetalleOpen = false;

    this.postDetalle = null;
  }
}