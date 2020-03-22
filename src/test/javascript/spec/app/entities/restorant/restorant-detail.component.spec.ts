import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { RestorantDetailComponent } from 'app/entities/restorant/restorant-detail.component';
import { Restorant } from 'app/shared/model/restorant.model';

describe('Component Tests', () => {
  describe('Restorant Management Detail Component', () => {
    let comp: RestorantDetailComponent;
    let fixture: ComponentFixture<RestorantDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ restorant: new Restorant(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [RestorantDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RestorantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RestorantDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load restorant on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.restorant).toEqual(jasmine.objectContaining({ id: 123 }));
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
