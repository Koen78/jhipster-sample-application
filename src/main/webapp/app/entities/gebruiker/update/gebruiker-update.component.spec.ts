jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GebruikerService } from '../service/gebruiker.service';
import { IGebruiker, Gebruiker } from '../gebruiker.model';

import { GebruikerUpdateComponent } from './gebruiker-update.component';

describe('Component Tests', () => {
  describe('Gebruiker Management Update Component', () => {
    let comp: GebruikerUpdateComponent;
    let fixture: ComponentFixture<GebruikerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let gebruikerService: GebruikerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GebruikerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GebruikerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GebruikerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      gebruikerService = TestBed.inject(GebruikerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const gebruiker: IGebruiker = { id: 456 };

        activatedRoute.data = of({ gebruiker });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(gebruiker));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gebruiker = { id: 123 };
        spyOn(gebruikerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gebruiker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gebruiker }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(gebruikerService.update).toHaveBeenCalledWith(gebruiker);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gebruiker = new Gebruiker();
        spyOn(gebruikerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gebruiker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gebruiker }));
        saveSubject.complete();

        // THEN
        expect(gebruikerService.create).toHaveBeenCalledWith(gebruiker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gebruiker = { id: 123 };
        spyOn(gebruikerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gebruiker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(gebruikerService.update).toHaveBeenCalledWith(gebruiker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
