import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolerUpdateComponent } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer-update.component';
import { KeyValuPairHolerService } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer.service';
import { KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

describe('Component Tests', () => {
  describe('KeyValuPairHoler Management Update Component', () => {
    let comp: KeyValuPairHolerUpdateComponent;
    let fixture: ComponentFixture<KeyValuPairHolerUpdateComponent>;
    let service: KeyValuPairHolerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(KeyValuPairHolerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KeyValuPairHolerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KeyValuPairHolerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new KeyValuPairHoler(123);
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
        const entity = new KeyValuPairHoler();
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
