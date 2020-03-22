import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { OrderedFoodDetailComponent } from 'app/entities/ordered-food/ordered-food-detail.component';
import { OrderedFood } from 'app/shared/model/ordered-food.model';

describe('Component Tests', () => {
  describe('OrderedFood Management Detail Component', () => {
    let comp: OrderedFoodDetailComponent;
    let fixture: ComponentFixture<OrderedFoodDetailComponent>;
    const route = ({ data: of({ orderedFood: new OrderedFood(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [OrderedFoodDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OrderedFoodDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrderedFoodDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load orderedFood on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orderedFood).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
