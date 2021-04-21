import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOverview, Overview } from '../overview.model';

import { OverviewService } from './overview.service';

describe('Service Tests', () => {
  describe('Overview Service', () => {
    let service: OverviewService;
    let httpMock: HttpTestingController;
    let elemDefault: IOverview;
    let expectedResult: IOverview | IOverview[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OverviewService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        objectId: 0,
        name: 'AAAAAAA',
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

      it('should create a Overview', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Overview()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Overview', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectId: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Overview', () => {
        const patchObject = Object.assign(
          {
            objectId: 1,
            name: 'BBBBBB',
          },
          new Overview()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Overview', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectId: 1,
            name: 'BBBBBB',
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

      it('should delete a Overview', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOverviewToCollectionIfMissing', () => {
        it('should add a Overview to an empty array', () => {
          const overview: IOverview = { id: 123 };
          expectedResult = service.addOverviewToCollectionIfMissing([], overview);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(overview);
        });

        it('should not add a Overview to an array that contains it', () => {
          const overview: IOverview = { id: 123 };
          const overviewCollection: IOverview[] = [
            {
              ...overview,
            },
            { id: 456 },
          ];
          expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, overview);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Overview to an array that doesn't contain it", () => {
          const overview: IOverview = { id: 123 };
          const overviewCollection: IOverview[] = [{ id: 456 }];
          expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, overview);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(overview);
        });

        it('should add only unique Overview to an array', () => {
          const overviewArray: IOverview[] = [{ id: 123 }, { id: 456 }, { id: 23977 }];
          const overviewCollection: IOverview[] = [{ id: 123 }];
          expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, ...overviewArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const overview: IOverview = { id: 123 };
          const overview2: IOverview = { id: 456 };
          expectedResult = service.addOverviewToCollectionIfMissing([], overview, overview2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(overview);
          expect(expectedResult).toContain(overview2);
        });

        it('should accept null and undefined values', () => {
          const overview: IOverview = { id: 123 };
          expectedResult = service.addOverviewToCollectionIfMissing([], null, overview, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(overview);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
