import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../modal/modal.component';
import { PostService } from '../../../core/services/post.service';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-detalle',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './post-detalle.component.html',
  styleUrls: ['./post-detalle.component.css']
})
export class PostDetalleComponent {

  @Input() isOpen = false;
  @Input() post: any;

  @Output() close = new EventEmitter<void>();
  @Output() accionRealizada = new EventEmitter<void>();

  loading = false;
  saldo = 0;
  guardado = false;

  constructor(
    private postService: PostService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit() {
    this.usuarioService.saldo$.subscribe(s => this.saldo = s);
    if (this.usuarioService.getSaldo() === 0) {
      this.usuarioService.getSaldoFromBackend().subscribe(s => {
        this.usuarioService.setSaldo(s);
      });
    }

    if (this.post?.id && this.post?.tipo) { // No funciona
      this.postService
        .estaGuardado(this.post.id, this.post.tipo)
        .subscribe(res => {
          this.guardado = res;
        });
    }
  }

  cerrar() {
    this.close.emit();
  }

  tieneSaldo(): boolean {
    return this.saldo >= (this.post?.precio || 0);
  }

  irASaldo() {
    this.cerrar();
    this.router.navigate(['/saldo']);
  }

  comprar() {
    if (!this.post?.id) return;

    this.loading = true;

    this.postService.comprar(this.post.id).subscribe({
      next: (nuevoSaldo) => {
        this.usuarioService.setSaldo(nuevoSaldo);
        alert('Compra realizada ✅');

        this.loading = false;
        this.accionRealizada.emit();
        this.cerrar();
      },
      error: err => {
        console.error(err);
        alert('Error al comprar ❌');
        this.loading = false;
      }
    });
  }

  intercambiar() {
    if (!this.post?.id) return;

    this.loading = true;

    this.postService.intercambiar(this.post.id).subscribe({
      next: () => {
        alert('Solicitud enviada 🔁');

        this.loading = false;
        this.accionRealizada.emit();
        this.cerrar();
      },
      error: err => {
        console.error(err);
        alert('Error en intercambio ❌');
        this.loading = false;
      }
    });
  }

  guardarPost() {

    if (!this.post?.id || !this.post?.tipo) return;

    if (this.guardado) {

      this.postService
        .eliminarGuardado(this.post.id, this.post.tipo)
        .subscribe({
          next: () => {
            this.guardado = false;
            alert('Eliminado de guardados ❌');
          },
          error: err => {
            console.error(err);
            alert('Error ❌');
          }
        });

    } else {

      this.postService
        .guardarPost(this.post.id, this.post.tipo)
        .subscribe({
          next: () => {
            this.guardado = true;
            alert('Guardado correctamente 🔖');
          },
          error: err => {
            console.error(err);
            alert('Error ❌');
          }
        });
    }
  }
}