import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'telegram-user',
        loadChildren: () => import('./telegram-user/telegram-user.module').then(m => m.DeliverEthTelegramUserModule)
      },
      {
        path: 'restorant',
        loadChildren: () => import('./restorant/restorant.module').then(m => m.DeliverEthRestorantModule)
      },
      {
        path: 'food',
        loadChildren: () => import('./food/food.module').then(m => m.DeliverEthFoodModule)
      },
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.DeliverEthOrderModule)
      },
      {
        path: 'ordered-food',
        loadChildren: () => import('./ordered-food/ordered-food.module').then(m => m.DeliverEthOrderedFoodModule)
      },
      {
        path: 'key-valu-pair-holder',
        loadChildren: () => import('./key-valu-pair-holder/key-valu-pair-holder.module').then(m => m.DeliverEthKeyValuPairHolderModule)
      },
      {
        path: 'telegram-restaurant-user',
        loadChildren: () =>
          import('./telegram-restaurant-user/telegram-restaurant-user.module').then(m => m.DeliverEthTelegramRestaurantUserModule)
      },
      {
        path: 'telegram-delivery-user',
        loadChildren: () =>
          import('./telegram-delivery-user/telegram-delivery-user.module').then(m => m.DeliverEthTelegramDeliveryUserModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class DeliverEthEntityModule {}
