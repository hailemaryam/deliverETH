import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITelegramDeliveryUser, TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';
import { TelegramDeliveryUserService } from './telegram-delivery-user.service';
import { IRestorant } from 'app/shared/model/restorant.model';
import { RestorantService } from 'app/entities/restorant/restorant.service';

@Component({
  selector: 'jhi-telegram-delivery-user-update',
  templateUrl: './telegram-delivery-user-update.component.html'
})
export class TelegramDeliveryUserUpdateComponent implements OnInit {
  isSaving = false;
  restorants: IRestorant[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    userName: [null, []],
    userId: [null, []],
    chatId: [],
    phone: [],
    conversationMetaData: [],
    loadedPage: [],
    status: [],
    currentBalance: [],
    currentLatitude: [],
    currentLongitude: [],
    restorants: []
  });

  constructor(
    protected telegramDeliveryUserService: TelegramDeliveryUserService,
    protected restorantService: RestorantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramDeliveryUser }) => {
      this.updateForm(telegramDeliveryUser);

      this.restorantService.query().subscribe((res: HttpResponse<IRestorant[]>) => (this.restorants = res.body || []));
    });
  }

  updateForm(telegramDeliveryUser: ITelegramDeliveryUser): void {
    this.editForm.patchValue({
      id: telegramDeliveryUser.id,
      firstName: telegramDeliveryUser.firstName,
      lastName: telegramDeliveryUser.lastName,
      userName: telegramDeliveryUser.userName,
      userId: telegramDeliveryUser.userId,
      chatId: telegramDeliveryUser.chatId,
      phone: telegramDeliveryUser.phone,
      conversationMetaData: telegramDeliveryUser.conversationMetaData,
      loadedPage: telegramDeliveryUser.loadedPage,
      status: telegramDeliveryUser.status,
      currentBalance: telegramDeliveryUser.currentBalance,
      currentLatitude: telegramDeliveryUser.currentLatitude,
      currentLongitude: telegramDeliveryUser.currentLongitude,
      restorants: telegramDeliveryUser.restorants
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const telegramDeliveryUser = this.createFromForm();
    if (telegramDeliveryUser.id !== undefined) {
      this.subscribeToSaveResponse(this.telegramDeliveryUserService.update(telegramDeliveryUser));
    } else {
      this.subscribeToSaveResponse(this.telegramDeliveryUserService.create(telegramDeliveryUser));
    }
  }

  private createFromForm(): ITelegramDeliveryUser {
    return {
      ...new TelegramDeliveryUser(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      chatId: this.editForm.get(['chatId'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      conversationMetaData: this.editForm.get(['conversationMetaData'])!.value,
      loadedPage: this.editForm.get(['loadedPage'])!.value,
      status: this.editForm.get(['status'])!.value,
      currentBalance: this.editForm.get(['currentBalance'])!.value,
      currentLatitude: this.editForm.get(['currentLatitude'])!.value,
      currentLongitude: this.editForm.get(['currentLongitude'])!.value,
      restorants: this.editForm.get(['restorants'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITelegramDeliveryUser>>): void {
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

  trackById(index: number, item: IRestorant): any {
    return item.id;
  }

  getSelected(selectedVals: IRestorant[], option: IRestorant): IRestorant {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
