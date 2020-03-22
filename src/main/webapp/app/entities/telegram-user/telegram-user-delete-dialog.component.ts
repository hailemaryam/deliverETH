import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelegramUser } from 'app/shared/model/telegram-user.model';
import { TelegramUserService } from './telegram-user.service';

@Component({
  templateUrl: './telegram-user-delete-dialog.component.html'
})
export class TelegramUserDeleteDialogComponent {
  telegramUser?: ITelegramUser;

  constructor(
    protected telegramUserService: TelegramUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.telegramUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('telegramUserListModification');
      this.activeModal.close();
    });
  }
}
