import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RestorantService } from 'app/entities/restorant/restorant.service';
import { IRestorant, Restorant } from 'app/shared/model/restorant.model';

describe('Service Tests', () => {
  describe('Restorant Service', () => {
    let injector: TestBed;
    let service: RestorantService;
    let httpMock: HttpTestingController;
    let elemDefault: IRestorant;
    let expectedResult: IRestorant | IRestorant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RestorantService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Restorant(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'image/png', 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Restorant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Restorant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Restorant', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            userName: 'BBBBBB',
            description: 'BBBBBB',
            iconImage: 'BBBBBB',
            latitude: 1,
            longtude: 1,
            availableOrderCap: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Restorant', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            userName: 'BBBBBB',
            description: 'BBBBBB',
            iconImage: 'BBBBBB',
            latitude: 1,
            longtude: 1,
            availableOrderCap: 1
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

      it('should delete a Restorant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
