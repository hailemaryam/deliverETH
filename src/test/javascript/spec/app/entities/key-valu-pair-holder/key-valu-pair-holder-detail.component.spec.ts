import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolderDetailComponent } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder-detail.component';
import { KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

describe('Component Tests', () => {
  describe('KeyValuPairHolder Management Detail Component', () => {
    let comp: KeyValuPairHolderDetailComponent;
    let fixture: ComponentFixture<KeyValuPairHolderDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ keyValuPairHolder: new KeyValuPairHolder(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolderDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(KeyValuPairHolderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KeyValuPairHolderDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load keyValuPairHolder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.keyValuPairHolder).toEqual(jasmine.objectContaining({ id: 123 }));
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
