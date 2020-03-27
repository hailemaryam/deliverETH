import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { KeyValuPairHolerDeleteDialogComponent } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer-delete-dialog.component';
import { KeyValuPairHolerService } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer.service';

describe('Component Tests', () => {
  describe('KeyValuPairHoler Management Delete Component', () => {
    let comp: KeyValuPairHolerDeleteDialogComponent;
    let fixture: ComponentFixture<KeyValuPairHolerDeleteDialogComponent>;
    let service: KeyValuPairHolerService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolerDeleteDialogComponent]
      })
        .overrideTemplate(KeyValuPairHolerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KeyValuPairHolerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KeyValuPairHolerService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
