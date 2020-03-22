import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DeliverEthTestModule } from '../../../test.module';
import { RestorantUpdateComponent } from 'app/entities/restorant/restorant-update.component';
import { RestorantService } from 'app/entities/restorant/restorant.service';
import { Restorant } from 'app/shared/model/restorant.model';

describe('Component Tests', () => {
  describe('Restorant Management Update Component', () => {
    let comp: RestorantUpdateComponent;
    let fixture: ComponentFixture<RestorantUpdateComponent>;
    let service: RestorantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [RestorantUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RestorantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RestorantUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RestorantService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Restorant(123);
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
        const entity = new Restorant();
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
