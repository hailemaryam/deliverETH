import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { OrderedFoodDetailComponent } from 'app/entities/ordered-food/ordered-food-detail.component';
import { OrderedFood } from 'app/shared/model/ordered-food.model';

describe('Component Tests', () => {
  describe('OrderedFood Management Detail Component', () => {
    let comp: OrderedFoodDetailComponent;
    let fixture: ComponentFixture<OrderedFoodDetailComponent>;
    let dataUtils: JhiDataUtils;
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
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load orderedFood on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orderedFood).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
