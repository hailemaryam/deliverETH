import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';
import { TelegramRestaurantUserService } from './telegram-restaurant-user.service';

@Component({
  templateUrl: './telegram-restaurant-user-delete-dialog.component.html'
})
export class TelegramRestaurantUserDeleteDialogComponent {
  telegramRestaurantUser?: ITelegramRestaurantUser;

  constructor(
    protected telegramRestaurantUserService: TelegramRestaurantUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.telegramRestaurantUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('telegramRestaurantUserListModification');
      this.activeModal.close();
    });
  }
}
