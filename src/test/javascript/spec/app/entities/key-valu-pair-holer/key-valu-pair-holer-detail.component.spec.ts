import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolerDetailComponent } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer-detail.component';
import { KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

describe('Component Tests', () => {
  describe('KeyValuPairHoler Management Detail Component', () => {
    let comp: KeyValuPairHolerDetailComponent;
    let fixture: ComponentFixture<KeyValuPairHolerDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ keyValuPairHoler: new KeyValuPairHoler(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(KeyValuPairHolerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KeyValuPairHolerDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load keyValuPairHoler on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.keyValuPairHoler).toEqual(jasmine.objectContaining({ id: 123 }));
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
