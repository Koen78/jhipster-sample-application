import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GebruikerDetailComponent } from './gebruiker-detail.component';

describe('Component Tests', () => {
  describe('Gebruiker Management Detail Component', () => {
    let comp: GebruikerDetailComponent;
    let fixture: ComponentFixture<GebruikerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GebruikerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ gebruiker: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GebruikerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GebruikerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load gebruiker on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gebruiker).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
