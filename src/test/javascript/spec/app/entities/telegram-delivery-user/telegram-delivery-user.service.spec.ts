import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TelegramDeliveryUserService } from 'app/entities/telegram-delivery-user/telegram-delivery-user.service';
import { ITelegramDeliveryUser, TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

describe('Service Tests', () => {
  describe('TelegramDeliveryUser Service', () => {
    let injector: TestBed;
    let service: TelegramDeliveryUserService;
    let httpMock: HttpTestingController;
    let elemDefault: ITelegramDeliveryUser;
    let expectedResult: ITelegramDeliveryUser | ITelegramDeliveryUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TelegramDeliveryUserService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new TelegramDeliveryUser(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TelegramDeliveryUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TelegramDeliveryUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TelegramDeliveryUser', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            userName: 'BBBBBB',
            userId: 1,
            chatId: 'BBBBBB',
            phone: 'BBBBBB',
            conversationMetaData: 'BBBBBB',
            loadedPage: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TelegramDeliveryUser', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            userName: 'BBBBBB',
            userId: 1,
            chatId: 'BBBBBB',
            phone: 'BBBBBB',
            conversationMetaData: 'BBBBBB',
            loadedPage: 1
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

      it('should delete a TelegramDeliveryUser', () => {
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
