jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OverviewService } from '../service/overview.service';
import { IOverview, Overview } from '../overview.model';
import { IGebruiker } from 'app/entities/gebruiker/gebruiker.model';
import { GebruikerService } from 'app/entities/gebruiker/service/gebruiker.service';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';

import { OverviewUpdateComponent } from './overview-update.component';

describe('Component Tests', () => {
  describe('Overview Management Update Component', () => {
    let comp: OverviewUpdateComponent;
    let fixture: ComponentFixture<OverviewUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let overviewService: OverviewService;
    let gebruikerService: GebruikerService;
    let documentTypeService: DocumentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OverviewUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OverviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OverviewUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      overviewService = TestBed.inject(OverviewService);
      gebruikerService = TestBed.inject(GebruikerService);
      documentTypeService = TestBed.inject(DocumentTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Gebruiker query and add missing value', () => {
        const overview: IOverview = { id: 456 };
        const gebruikers: IGebruiker[] = [{ id: 55431 }];
        overview.gebruikers = gebruikers;

        const gebruikerCollection: IGebruiker[] = [{ id: 90561 }];
        spyOn(gebruikerService, 'query').and.returnValue(of(new HttpResponse({ body: gebruikerCollection })));
        const additionalGebruikers = [...gebruikers];
        const expectedCollection: IGebruiker[] = [...additionalGebruikers, ...gebruikerCollection];
        spyOn(gebruikerService, 'addGebruikerToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        expect(gebruikerService.query).toHaveBeenCalled();
        expect(gebruikerService.addGebruikerToCollectionIfMissing).toHaveBeenCalledWith(gebruikerCollection, ...additionalGebruikers);
        expect(comp.gebruikersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DocumentType query and add missing value', () => {
        const overview: IOverview = { id: 456 };
        const documentTypes: IDocumentType[] = [{ id: 5162 }];
        overview.documentTypes = documentTypes;

        const documentTypeCollection: IDocumentType[] = [{ id: 98794 }];
        spyOn(documentTypeService, 'query').and.returnValue(of(new HttpResponse({ body: documentTypeCollection })));
        const additionalDocumentTypes = [...documentTypes];
        const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
        spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        expect(documentTypeService.query).toHaveBeenCalled();
        expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
          documentTypeCollection,
          ...additionalDocumentTypes
        );
        expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const overview: IOverview = { id: 456 };
        const gebruikers: IGebruiker = { id: 28975 };
        overview.gebruikers = [gebruikers];
        const documentTypes: IDocumentType = { id: 62268 };
        overview.documentTypes = [documentTypes];

        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(overview));
        expect(comp.gebruikersSharedCollection).toContain(gebruikers);
        expect(comp.documentTypesSharedCollection).toContain(documentTypes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const overview = { id: 123 };
        spyOn(overviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: overview }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(overviewService.update).toHaveBeenCalledWith(overview);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const overview = new Overview();
        spyOn(overviewService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: overview }));
        saveSubject.complete();

        // THEN
        expect(overviewService.create).toHaveBeenCalledWith(overview);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const overview = { id: 123 };
        spyOn(overviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ overview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(overviewService.update).toHaveBeenCalledWith(overview);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackGebruikerById', () => {
        it('Should return tracked Gebruiker primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGebruikerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDocumentTypeById', () => {
        it('Should return tracked DocumentType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedGebruiker', () => {
        it('Should return option if no Gebruiker is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedGebruiker(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Gebruiker for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedGebruiker(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Gebruiker is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedGebruiker(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedDocumentType', () => {
        it('Should return option if no DocumentType is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedDocumentType(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected DocumentType for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedDocumentType(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this DocumentType is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedDocumentType(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
