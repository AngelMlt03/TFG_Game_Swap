import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-footer',
  template: `
    <footer class="footer">
      <p>©2026 GameSwap</p>
      <p>Contacto: gameswap&#64;email.com</p>
    </footer>
  `,
  styles: [`
    .footer {
      background: #111;
      color: white;
      text-align: center;
      padding: 1rem;
    }
  `]
})
export class FooterComponent {}