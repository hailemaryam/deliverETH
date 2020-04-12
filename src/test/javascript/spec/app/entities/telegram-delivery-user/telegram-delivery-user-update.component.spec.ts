import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramDeliveryUserUpdateComponent } from 'app/entities/telegram-delivery-user/telegram-delivery-user-update.component';
import { TelegramDeliveryUserService } from 'app/entities/telegram-delivery-user/telegram-delivery-user.service';
import { TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

describe('Component Tests', () => {
  describe('TelegramDeliveryUser Management Update Component', () => {
    let comp: TelegramDeliveryUserUpdateComponent;
    let fixture: ComponentFixture<TelegramDeliveryUserUpdateComponent>;
    let service: TelegramDeliveryUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramDeliveryUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TelegramDeliveryUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TelegramDeliveryUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TelegramDeliveryUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TelegramDeliveryUser(123);
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
        const entity = new TelegramDeliveryUser();
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
