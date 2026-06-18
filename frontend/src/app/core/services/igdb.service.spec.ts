import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';

import { IgdbService } from './igdb.service';
import { environment } from '../../../environments/environment';

describe('IgdbService', () => {
  let service: IgdbService;
  let httpMock: HttpTestingController;

  const apiUrl = `${environment.apiUrl}/igdb`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IgdbService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(IgdbService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('buscarJuegos sin franchiseId', () => {
    let result: any;

    service.buscarJuegos('Zelda').subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/games?query=Zelda`);

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        id: 1,
        name: 'Zelda',
        cover: {
          image_id: 'abc123',
        },
      },
    ]);

    expect(result.length).toBe(1);
    expect(result[0].id).toBe(1);
    expect(result[0].nombre).toBe('Zelda');
    expect(result[0].imagen).toContain('abc123');
  });

  it('buscarJuegos con franchiseId', () => {
    service.buscarJuegos('Mario', 10).subscribe();

    const req = httpMock.expectOne(
      `${apiUrl}/games?query=Mario&franchiseId=10`,
    );

    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  it('buscarJuegos sin cover', () => {
    let result: any;

    service.buscarJuegos('Test').subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/games?query=Test`);

    req.flush([
      {
        id: 5,
        name: 'Juego sin cover',
      },
    ]);

    expect(result[0].imagen).toBeNull();
  });

  it('buscarFranquicias', () => {
    let result: any;

    service.buscarFranquicias('Pokemon').subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/franchises?query=Pokemon`);

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        name: 'Pokemon',
      },
    ]);

    expect(result[0].nombre).toBe('Pokemon');
    expect(result[0].imagen).toBeNull();
  });

  it('buscarPlataformas con logo', () => {
    let result: any;

    service.buscarPlataformas('PlayStation').subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/platforms?query=PlayStation`);

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        name: 'PS5',
        platform_logo: {
          image_id: 'logo123',
        },
      },
    ]);

    expect(result[0].nombre).toBe('PS5');
    expect(result[0].imagen).toContain('logo123');
  });

  it('buscarPlataformas sin logo', () => {
    let result: any;

    service.buscarPlataformas('PC').subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/platforms?query=PC`);

    req.flush([
      {
        name: 'PC',
      },
    ]);

    expect(result[0].imagen).toBeNull();
  });

  it('getCover con imagen', () => {
    let result: string | null = null;

    service.getCover(99).subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/cover?id=99`);

    expect(req.request.method).toBe('GET');

    req.flush([
      {
        cover: {
          image_id: 'cover999',
        },
      },
    ]);

    expect(result).toContain('cover999');
  });

  it('getCover sin imagen', () => {
    let result: string | null = 'x';

    service.getCover(99).subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/cover?id=99`);

    req.flush([{}]);

    expect(result).toBeNull();
  });

  it('getGameDetails', () => {
    const response = [
      {
        id: 1,
        name: 'Zelda',
      },
    ];

    let result: any;

    service.getGameDetails(1).subscribe((res) => {
      result = res;
    });

    const req = httpMock.expectOne(`${apiUrl}/game-details?id=1`);

    expect(req.request.method).toBe('GET');

    req.flush(response);

    expect(result).toEqual(response);
  });
});
