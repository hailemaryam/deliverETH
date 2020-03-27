import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { KeyValuPairHolerService } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer.service';
import { IKeyValuPairHoler, KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

describe('Service Tests', () => {
  describe('KeyValuPairHoler Service', () => {
    let injector: TestBed;
    let service: KeyValuPairHolerService;
    let httpMock: HttpTestingController;
    let elemDefault: IKeyValuPairHoler;
    let expectedResult: IKeyValuPairHoler | IKeyValuPairHoler[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(KeyValuPairHolerService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new KeyValuPairHoler(0, 'AAAAAAA', 'AAAAAAA', 0, 'image/png', 'AAAAAAA', 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a KeyValuPairHoler', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new KeyValuPairHoler()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a KeyValuPairHoler', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            valueString: 'BBBBBB',
            valueNumber: 1,
            valueImage: 'BBBBBB',
            valueBlob: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of KeyValuPairHoler', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            valueString: 'BBBBBB',
            valueNumber: 1,
            valueImage: 'BBBBBB',
            valueBlob: 'BBBBBB'
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

      it('should delete a KeyValuPairHoler', () => {
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
