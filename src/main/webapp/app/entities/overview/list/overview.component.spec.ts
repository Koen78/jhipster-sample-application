import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OverviewService } from '../service/overview.service';

import { OverviewComponent } from './overview.component';

describe('Component Tests', () => {
  describe('Overview Management Component', () => {
    let comp: OverviewComponent;
    let fixture: ComponentFixture<OverviewComponent>;
    let service: OverviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OverviewComponent],
      })
        .overrideTemplate(OverviewComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OverviewComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OverviewService);

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
      expect(comp.overviews?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
