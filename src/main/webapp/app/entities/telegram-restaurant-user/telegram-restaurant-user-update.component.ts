import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITelegramRestaurantUser, TelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';
import { TelegramRestaurantUserService } from './telegram-restaurant-user.service';
import { IRestorant } from 'app/shared/model/restorant.model';
import { RestorantService } from 'app/entities/restorant/restorant.service';

@Component({
  selector: 'jhi-telegram-restaurant-user-update',
  templateUrl: './telegram-restaurant-user-update.component.html'
})
export class TelegramRestaurantUserUpdateComponent implements OnInit {
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
    restorants: []
  });

  constructor(
    protected telegramRestaurantUserService: TelegramRestaurantUserService,
    protected restorantService: RestorantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramRestaurantUser }) => {
      this.updateForm(telegramRestaurantUser);

      this.restorantService.query().subscribe((res: HttpResponse<IRestorant[]>) => (this.restorants = res.body || []));
    });
  }

  updateForm(telegramRestaurantUser: ITelegramRestaurantUser): void {
    this.editForm.patchValue({
      id: telegramRestaurantUser.id,
      firstName: telegramRestaurantUser.firstName,
      lastName: telegramRestaurantUser.lastName,
      userName: telegramRestaurantUser.userName,
      userId: telegramRestaurantUser.userId,
      chatId: telegramRestaurantUser.chatId,
      phone: telegramRestaurantUser.phone,
      conversationMetaData: telegramRestaurantUser.conversationMetaData,
      loadedPage: telegramRestaurantUser.loadedPage,
      status: telegramRestaurantUser.status,
      currentBalance: telegramRestaurantUser.currentBalance,
      restorants: telegramRestaurantUser.restorants
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const telegramRestaurantUser = this.createFromForm();
    if (telegramRestaurantUser.id !== undefined) {
      this.subscribeToSaveResponse(this.telegramRestaurantUserService.update(telegramRestaurantUser));
    } else {
      this.subscribeToSaveResponse(this.telegramRestaurantUserService.create(telegramRestaurantUser));
    }
  }

  private createFromForm(): ITelegramRestaurantUser {
    return {
      ...new TelegramRestaurantUser(),
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
      restorants: this.editForm.get(['restorants'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITelegramRestaurantUser>>): void {
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
