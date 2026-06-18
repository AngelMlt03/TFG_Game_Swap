import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent {
  year = new Date().getFullYear();

  abrirGithub() {
    window.open(
      'https://github.com/AngelMlt03/TFG_Game_Swap',
      '_blank',
      'noopener,noreferrer');
  }

  abrirSwagger() {
    window.open(
      'https://tfg-game-swap-backend.onrender.com/swagger-ui/index.html',
      '_blank',
      'noopener,noreferrer'
    );
  }
}
