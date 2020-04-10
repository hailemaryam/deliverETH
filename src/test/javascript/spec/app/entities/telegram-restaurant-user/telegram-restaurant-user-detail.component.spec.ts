import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramRestaurantUserDetailComponent } from 'app/entities/telegram-restaurant-user/telegram-restaurant-user-detail.component';
import { TelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

describe('Component Tests', () => {
  describe('TelegramRestaurantUser Management Detail Component', () => {
    let comp: TelegramRestaurantUserDetailComponent;
    let fixture: ComponentFixture<TelegramRestaurantUserDetailComponent>;
    const route = ({ data: of({ telegramRestaurantUser: new TelegramRestaurantUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramRestaurantUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TelegramRestaurantUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TelegramRestaurantUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load telegramRestaurantUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.telegramRestaurantUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
