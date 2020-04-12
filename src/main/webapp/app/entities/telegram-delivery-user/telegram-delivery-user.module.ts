import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { TelegramDeliveryUserComponent } from './telegram-delivery-user.component';
import { TelegramDeliveryUserDetailComponent } from './telegram-delivery-user-detail.component';
import { TelegramDeliveryUserUpdateComponent } from './telegram-delivery-user-update.component';
import { TelegramDeliveryUserDeleteDialogComponent } from './telegram-delivery-user-delete-dialog.component';
import { telegramDeliveryUserRoute } from './telegram-delivery-user.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(telegramDeliveryUserRoute)],
  declarations: [
    TelegramDeliveryUserComponent,
    TelegramDeliveryUserDetailComponent,
    TelegramDeliveryUserUpdateComponent,
    TelegramDeliveryUserDeleteDialogComponent
  ],
  entryComponents: [TelegramDeliveryUserDeleteDialogComponent]
})
export class DeliverEthTelegramDeliveryUserModule {}
