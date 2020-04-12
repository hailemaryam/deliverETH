import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramDeliveryUserDetailComponent } from 'app/entities/telegram-delivery-user/telegram-delivery-user-detail.component';
import { TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

describe('Component Tests', () => {
  describe('TelegramDeliveryUser Management Detail Component', () => {
    let comp: TelegramDeliveryUserDetailComponent;
    let fixture: ComponentFixture<TelegramDeliveryUserDetailComponent>;
    const route = ({ data: of({ telegramDeliveryUser: new TelegramDeliveryUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramDeliveryUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TelegramDeliveryUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TelegramDeliveryUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load telegramDeliveryUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.telegramDeliveryUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
