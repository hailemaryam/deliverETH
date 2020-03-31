import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITelegramUser, TelegramUser } from 'app/shared/model/telegram-user.model';
import { TelegramUserService } from './telegram-user.service';

@Component({
  selector: 'jhi-telegram-user-update',
  templateUrl: './telegram-user-update.component.html'
})
export class TelegramUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    userName: [null, []],
    chatId: [],
    phone: [],
    conversationMetaData: [],
    orderIdPaused: [],
    orderedFoodIdPaused: [],
    selectedRestorant: [],
    loadedPage: []
  });

  constructor(protected telegramUserService: TelegramUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ telegramUser }) => {
      this.updateForm(telegramUser);
    });
  }

  updateForm(telegramUser: ITelegramUser): void {
    this.editForm.patchValue({
      id: telegramUser.id,
      firstName: telegramUser.firstName,
      lastName: telegramUser.lastName,
      userName: telegramUser.userName,
      chatId: telegramUser.chatId,
      phone: telegramUser.phone,
      conversationMetaData: telegramUser.conversationMetaData,
      orderIdPaused: telegramUser.orderIdPaused,
      orderedFoodIdPaused: telegramUser.orderedFoodIdPaused,
      selectedRestorant: telegramUser.selectedRestorant,
      loadedPage: telegramUser.loadedPage
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const telegramUser = this.createFromForm();
    if (telegramUser.id !== undefined) {
      this.subscribeToSaveResponse(this.telegramUserService.update(telegramUser));
    } else {
      this.subscribeToSaveResponse(this.telegramUserService.create(telegramUser));
    }
  }

  private createFromForm(): ITelegramUser {
    return {
      ...new TelegramUser(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      chatId: this.editForm.get(['chatId'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      conversationMetaData: this.editForm.get(['conversationMetaData'])!.value,
      orderIdPaused: this.editForm.get(['orderIdPaused'])!.value,
      orderedFoodIdPaused: this.editForm.get(['orderedFoodIdPaused'])!.value,
      selectedRestorant: this.editForm.get(['selectedRestorant'])!.value,
      loadedPage: this.editForm.get(['loadedPage'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITelegramUser>>): void {
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
}
