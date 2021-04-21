import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OverviewDetailComponent } from './overview-detail.component';

describe('Component Tests', () => {
  describe('Overview Management Detail Component', () => {
    let comp: OverviewDetailComponent;
    let fixture: ComponentFixture<OverviewDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OverviewDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ overview: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OverviewDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OverviewDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load overview on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.overview).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
