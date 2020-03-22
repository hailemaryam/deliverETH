import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { TelegramUserComponent } from './telegram-user.component';
import { TelegramUserDetailComponent } from './telegram-user-detail.component';
import { TelegramUserUpdateComponent } from './telegram-user-update.component';
import { TelegramUserDeleteDialogComponent } from './telegram-user-delete-dialog.component';
import { telegramUserRoute } from './telegram-user.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(telegramUserRoute)],
  declarations: [TelegramUserComponent, TelegramUserDetailComponent, TelegramUserUpdateComponent, TelegramUserDeleteDialogComponent],
  entryComponents: [TelegramUserDeleteDialogComponent]
})
export class DeliverEthTelegramUserModule {}
