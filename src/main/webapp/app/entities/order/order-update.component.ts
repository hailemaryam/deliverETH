import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITelegramUser } from 'app/shared/model/telegram-user.model';
import { TelegramUserService } from 'app/entities/telegram-user/telegram-user.service';
import { ITelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';
import { TelegramDeliveryUserService } from 'app/entities/telegram-delivery-user/telegram-delivery-user.service';

type SelectableEntity = ITelegramUser | ITelegramDeliveryUser;

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  telegramusers: ITelegramUser[] = [];
  telegramdeliveryusers: ITelegramDeliveryUser[] = [];

  editForm = this.fb.group({
    id: [],
    latitude: [],
    longtude: [],
    locationDescription: [],
    totalPrice: [],
    transportationFee: [],
    date: [],
    additionalNote: [],
    orderStatus: [],
    telegramUserId: [],
    telegramDeliveryUserId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected orderService: OrderService,
    protected telegramUserService: TelegramUserService,
    protected telegramDeliveryUserService: TelegramDeliveryUserService,
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

      this.telegramDeliveryUserService
        .query()
        .subscribe((res: HttpResponse<ITelegramDeliveryUser[]>) => (this.telegramdeliveryusers = res.body || []));
    });
  }

  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      latitude: order.latitude,
      longtude: order.longtude,
      locationDescription: order.locationDescription,
      totalPrice: order.totalPrice,
      transportationFee: order.transportationFee,
      date: order.date ? order.date.format(DATE_TIME_FORMAT) : null,
      additionalNote: order.additionalNote,
      orderStatus: order.orderStatus,
      telegramUserId: order.telegramUserId,
      telegramDeliveryUserId: order.telegramDeliveryUserId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('deliverEthApp.error', { message: err.message })
      );
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
      locationDescription: this.editForm.get(['locationDescription'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      transportationFee: this.editForm.get(['transportationFee'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      additionalNote: this.editForm.get(['additionalNote'])!.value,
      orderStatus: this.editForm.get(['orderStatus'])!.value,
      telegramUserId: this.editForm.get(['telegramUserId'])!.value,
      telegramDeliveryUserId: this.editForm.get(['telegramDeliveryUserId'])!.value
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
