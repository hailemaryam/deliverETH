import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { TelegramRestaurantUserUpdateComponent } from 'app/entities/telegram-restaurant-user/telegram-restaurant-user-update.component';
import { TelegramRestaurantUserService } from 'app/entities/telegram-restaurant-user/telegram-restaurant-user.service';
import { TelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

describe('Component Tests', () => {
  describe('TelegramRestaurantUser Management Update Component', () => {
    let comp: TelegramRestaurantUserUpdateComponent;
    let fixture: ComponentFixture<TelegramRestaurantUserUpdateComponent>;
    let service: TelegramRestaurantUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramRestaurantUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TelegramRestaurantUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TelegramRestaurantUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TelegramRestaurantUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TelegramRestaurantUser(123);
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
        const entity = new TelegramRestaurantUser();
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
