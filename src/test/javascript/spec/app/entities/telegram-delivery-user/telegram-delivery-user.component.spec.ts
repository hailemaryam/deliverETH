import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramDeliveryUserComponent } from 'app/entities/telegram-delivery-user/telegram-delivery-user.component';
import { TelegramDeliveryUserService } from 'app/entities/telegram-delivery-user/telegram-delivery-user.service';
import { TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

describe('Component Tests', () => {
  describe('TelegramDeliveryUser Management Component', () => {
    let comp: TelegramDeliveryUserComponent;
    let fixture: ComponentFixture<TelegramDeliveryUserComponent>;
    let service: TelegramDeliveryUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramDeliveryUserComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(TelegramDeliveryUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TelegramDeliveryUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TelegramDeliveryUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TelegramDeliveryUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.telegramDeliveryUsers && comp.telegramDeliveryUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TelegramDeliveryUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.telegramDeliveryUsers && comp.telegramDeliveryUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
