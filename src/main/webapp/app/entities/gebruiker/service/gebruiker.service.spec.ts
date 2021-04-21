import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGebruiker, Gebruiker } from '../gebruiker.model';

import { GebruikerService } from './gebruiker.service';

describe('Service Tests', () => {
  describe('Gebruiker Service', () => {
    let service: GebruikerService;
    let httpMock: HttpTestingController;
    let elemDefault: IGebruiker;
    let expectedResult: IGebruiker | IGebruiker[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GebruikerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        objectId: 0,
        naam: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Gebruiker', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Gebruiker()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Gebruiker', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectId: 1,
            naam: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Gebruiker', () => {
        const patchObject = Object.assign(
          {
            objectId: 1,
            naam: 'BBBBBB',
          },
          new Gebruiker()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Gebruiker', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectId: 1,
            naam: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Gebruiker', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGebruikerToCollectionIfMissing', () => {
        it('should add a Gebruiker to an empty array', () => {
          const gebruiker: IGebruiker = { id: 123 };
          expectedResult = service.addGebruikerToCollectionIfMissing([], gebruiker);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gebruiker);
        });

        it('should not add a Gebruiker to an array that contains it', () => {
          const gebruiker: IGebruiker = { id: 123 };
          const gebruikerCollection: IGebruiker[] = [
            {
              ...gebruiker,
            },
            { id: 456 },
          ];
          expectedResult = service.addGebruikerToCollectionIfMissing(gebruikerCollection, gebruiker);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Gebruiker to an array that doesn't contain it", () => {
          const gebruiker: IGebruiker = { id: 123 };
          const gebruikerCollection: IGebruiker[] = [{ id: 456 }];
          expectedResult = service.addGebruikerToCollectionIfMissing(gebruikerCollection, gebruiker);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gebruiker);
        });

        it('should add only unique Gebruiker to an array', () => {
          const gebruikerArray: IGebruiker[] = [{ id: 123 }, { id: 456 }, { id: 71710 }];
          const gebruikerCollection: IGebruiker[] = [{ id: 123 }];
          expectedResult = service.addGebruikerToCollectionIfMissing(gebruikerCollection, ...gebruikerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const gebruiker: IGebruiker = { id: 123 };
          const gebruiker2: IGebruiker = { id: 456 };
          expectedResult = service.addGebruikerToCollectionIfMissing([], gebruiker, gebruiker2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gebruiker);
          expect(expectedResult).toContain(gebruiker2);
        });

        it('should accept null and undefined values', () => {
          const gebruiker: IGebruiker = { id: 123 };
          expectedResult = service.addGebruikerToCollectionIfMissing([], null, gebruiker, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gebruiker);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
