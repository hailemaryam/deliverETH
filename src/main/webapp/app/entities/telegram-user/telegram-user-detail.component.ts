import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITelegramUser } from 'app/shared/model/telegram-user.model';

@Component({
  selector: 'jhi-telegram-user-detail',
  templateUrl: './telegram-user-detail.component.html'
})
export class TelegramUserDetailComponent implements OnInit {
  telegramUser: ITelegramUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramUser }) => (this.telegramUser = telegramUser));
  }

  previousState(): void {
    window.history.back();
  }
}
