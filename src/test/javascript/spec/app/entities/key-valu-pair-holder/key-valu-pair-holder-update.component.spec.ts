import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolderUpdateComponent } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder-update.component';
import { KeyValuPairHolderService } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder.service';
import { KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

describe('Component Tests', () => {
  describe('KeyValuPairHolder Management Update Component', () => {
    let comp: KeyValuPairHolderUpdateComponent;
    let fixture: ComponentFixture<KeyValuPairHolderUpdateComponent>;
    let service: KeyValuPairHolderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolderUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(KeyValuPairHolderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KeyValuPairHolderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KeyValuPairHolderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new KeyValuPairHolder(123);
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
        const entity = new KeyValuPairHolder();
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
