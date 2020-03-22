import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { ITelegramUser } from 'app/shared/model/telegram-user.model';
import { TelegramUserService } from 'app/entities/telegram-user/telegram-user.service';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  telegramusers: ITelegramUser[] = [];

  editForm = this.fb.group({
    id: [],
    latitude: [],
    longtude: [],
    totalPrice: [],
    date: [],
    telegramUserId: []
  });

  constructor(
    protected orderService: OrderService,
    protected telegramUserService: TelegramUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      if (!order.id) {
        const today = moment().startOf('day');
        order.date = today;
      }

      this.updateForm(order);

      this.telegramUserService.query().subscribe((res: HttpResponse<ITelegramUser[]>) => (this.telegramusers = res.body || []));
    });
  }

  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      latitude: order.latitude,
      longtude: order.longtude,
      totalPrice: order.totalPrice,
      date: order.date ? order.date.format(DATE_TIME_FORMAT) : null,
      telegramUserId: order.telegramUserId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longtude: this.editForm.get(['longtude'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      telegramUserId: this.editForm.get(['telegramUserId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ITelegramUser): any {
    return item.id;
  }
}
