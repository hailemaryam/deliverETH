import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';
import { TelegramDeliveryUserService } from './telegram-delivery-user.service';

@Component({
  templateUrl: './telegram-delivery-user-delete-dialog.component.html'
})
export class TelegramDeliveryUserDeleteDialogComponent {
  telegramDeliveryUser?: ITelegramDeliveryUser;

  constructor(
    protected telegramDeliveryUserService: TelegramDeliveryUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.telegramDeliveryUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('telegramDeliveryUserListModification');
      this.activeModal.close();
    });
  }
}
