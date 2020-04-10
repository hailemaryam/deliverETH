import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { TelegramRestaurantUserComponent } from './telegram-restaurant-user.component';
import { TelegramRestaurantUserDetailComponent } from './telegram-restaurant-user-detail.component';
import { TelegramRestaurantUserUpdateComponent } from './telegram-restaurant-user-update.component';
import { TelegramRestaurantUserDeleteDialogComponent } from './telegram-restaurant-user-delete-dialog.component';
import { telegramRestaurantUserRoute } from './telegram-restaurant-user.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(telegramRestaurantUserRoute)],
  declarations: [
    TelegramRestaurantUserComponent,
    TelegramRestaurantUserDetailComponent,
    TelegramRestaurantUserUpdateComponent,
    TelegramRestaurantUserDeleteDialogComponent
  ],
  entryComponents: [TelegramRestaurantUserDeleteDialogComponent]
})
export class DeliverEthTelegramRestaurantUserModule {}
