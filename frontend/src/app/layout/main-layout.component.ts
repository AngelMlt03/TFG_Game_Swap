import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from '../shared/components/footer/footer.component';
import { NavbarComponent } from '../shared/components/navbar/navbar.component';

@Component({
  standalone: true,
  selector: 'app-main-layout',
  imports: [RouterOutlet, NavbarComponent, FooterComponent],
  template: `
    <app-navbar></app-navbar>

    <main class="content">
      <router-outlet></router-outlet>
    </main>

    <app-footer></app-footer>
  `,
  styles: [`
    .content {
      min-height: 80vh;
      padding: 1rem;
    }
  `]
})
export class MainLayoutComponent {}