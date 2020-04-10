import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

@Component({
  selector: 'jhi-telegram-restaurant-user-detail',
  templateUrl: './telegram-restaurant-user-detail.component.html'
})
export class TelegramRestaurantUserDetailComponent implements OnInit {
  telegramRestaurantUser: ITelegramRestaurantUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramRestaurantUser }) => (this.telegramRestaurantUser = telegramRestaurantUser));
  }

  previousState(): void {
    window.history.back();
  }
}
