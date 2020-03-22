import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramUserUpdateComponent } from 'app/entities/telegram-user/telegram-user-update.component';
import { TelegramUserService } from 'app/entities/telegram-user/telegram-user.service';
import { TelegramUser } from 'app/shared/model/telegram-user.model';

describe('Component Tests', () => {
  describe('TelegramUser Management Update Component', () => {
    let comp: TelegramUserUpdateComponent;
    let fixture: ComponentFixture<TelegramUserUpdateComponent>;
    let service: TelegramUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TelegramUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TelegramUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TelegramUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TelegramUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TelegramUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
