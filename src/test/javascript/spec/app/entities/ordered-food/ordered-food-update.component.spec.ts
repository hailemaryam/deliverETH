import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { OrderedFoodUpdateComponent } from 'app/entities/ordered-food/ordered-food-update.component';
import { OrderedFoodService } from 'app/entities/ordered-food/ordered-food.service';
import { OrderedFood } from 'app/shared/model/ordered-food.model';

describe('Component Tests', () => {
  describe('OrderedFood Management Update Component', () => {
    let comp: OrderedFoodUpdateComponent;
    let fixture: ComponentFixture<OrderedFoodUpdateComponent>;
    let service: OrderedFoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [OrderedFoodUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OrderedFoodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderedFoodUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrderedFoodService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new OrderedFood(123);
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
        const entity = new OrderedFood();
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
