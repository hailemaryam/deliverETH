import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramUserDetailComponent } from 'app/entities/telegram-user/telegram-user-detail.component';
import { TelegramUser } from 'app/shared/model/telegram-user.model';

describe('Component Tests', () => {
  describe('TelegramUser Management Detail Component', () => {
    let comp: TelegramUserDetailComponent;
    let fixture: ComponentFixture<TelegramUserDetailComponent>;
    const route = ({ data: of({ telegramUser: new TelegramUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TelegramUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TelegramUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load telegramUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.telegramUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
