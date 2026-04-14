import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-home',
  template: `
    <div class="home">

      <h1>Bienvenido a GameSwap 🎮</h1>

      <p>Compra, vende e intercambia videojuegos fácilmente.</p>

      <div class="cards">
        <div class="card">
          <h3>Explorar juegos</h3>
          <p>Descubre lo que otros usuarios ofrecen</p>
        </div>

        <div class="card">
          <h3>Publicar</h3>
          <p>Vende o intercambia tus juegos</p>
        </div>

        <div class="card">
          <h3>Intercambios</h3>
          <p>Gestiona tus propuestas</p>
        </div>
      </div>

    </div>
  `,
  styles: [`
    .home {
      text-align: center;
      padding: 1rem;
    }

    .cards {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1rem;
      margin-top: 2rem;
    }

    .card {
      padding: 1rem;
      border-radius: 10px;
      background: #f5f5f5;
    }

    @media (max-width: 768px) {
      .cards {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class HomeComponent {}