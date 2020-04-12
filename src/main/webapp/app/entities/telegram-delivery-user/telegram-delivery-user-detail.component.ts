import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

@Component({
  selector: 'jhi-telegram-delivery-user-detail',
  templateUrl: './telegram-delivery-user-detail.component.html'
})
export class TelegramDeliveryUserDetailComponent implements OnInit {
  telegramDeliveryUser: ITelegramDeliveryUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramDeliveryUser }) => (this.telegramDeliveryUser = telegramDeliveryUser));
  }

  previousState(): void {
    window.history.back();
  }
}
