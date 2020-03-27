import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { KeyValuPairHolderService } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder.service';
import { IKeyValuPairHolder, KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

describe('Service Tests', () => {
  describe('KeyValuPairHolder Service', () => {
    let injector: TestBed;
    let service: KeyValuPairHolderService;
    let httpMock: HttpTestingController;
    let elemDefault: IKeyValuPairHolder;
    let expectedResult: IKeyValuPairHolder | IKeyValuPairHolder[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(KeyValuPairHolderService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new KeyValuPairHolder(0, 'AAAAAAA', 'AAAAAAA', 0, 'image/png', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a KeyValuPairHolder', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new KeyValuPairHolder()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a KeyValuPairHolder', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            valueString: 'BBBBBB',
            valueNumber: 1,
            valueImage: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of KeyValuPairHolder', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            valueString: 'BBBBBB',
            valueNumber: 1,
            valueImage: 'BBBBBB'
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

      it('should delete a KeyValuPairHolder', () => {
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
