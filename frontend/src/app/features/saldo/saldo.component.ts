import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-saldo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './saldo.component.html',
  styleUrls: ['./saldo.component.css'],
})
export class SaldoComponent {
  cantidad = 10;

  loading = false;

   private apiUrl = `${environment.apiUrl}/pagos`;

  constructor(private http: HttpClient) {}

  pagar() {
    this.loading = true;

    this.http
      .post(
        `${this.apiUrl}/checkout?cantidad=${this.cantidad}`,
        {},
        {
          responseType: 'text',
        },
      )
      .subscribe({
        next: (url) => {
          window.location.href = url;
        },

        error: (err) => {
          console.error(err);

          this.loading = false;
        },
      });
  }
}
