import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DeliverEthTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { TelegramDeliveryUserDeleteDialogComponent } from 'app/entities/telegram-delivery-user/telegram-delivery-user-delete-dialog.component';
import { TelegramDeliveryUserService } from 'app/entities/telegram-delivery-user/telegram-delivery-user.service';

describe('Component Tests', () => {
  describe('TelegramDeliveryUser Management Delete Component', () => {
    let comp: TelegramDeliveryUserDeleteDialogComponent;
    let fixture: ComponentFixture<TelegramDeliveryUserDeleteDialogComponent>;
    let service: TelegramDeliveryUserService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [TelegramDeliveryUserDeleteDialogComponent]
      })
        .overrideTemplate(TelegramDeliveryUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TelegramDeliveryUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TelegramDeliveryUserService);
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
