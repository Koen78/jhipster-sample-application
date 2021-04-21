import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GebruikerService } from '../service/gebruiker.service';

import { GebruikerComponent } from './gebruiker.component';

describe('Component Tests', () => {
  describe('Gebruiker Management Component', () => {
    let comp: GebruikerComponent;
    let fixture: ComponentFixture<GebruikerComponent>;
    let service: GebruikerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GebruikerComponent],
      })
        .overrideTemplate(GebruikerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GebruikerComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GebruikerService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.gebruikers?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
