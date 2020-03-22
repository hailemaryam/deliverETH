import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { OrderedFoodComponent } from './ordered-food.component';
import { OrderedFoodDetailComponent } from './ordered-food-detail.component';
import { OrderedFoodUpdateComponent } from './ordered-food-update.component';
import { OrderedFoodDeleteDialogComponent } from './ordered-food-delete-dialog.component';
import { orderedFoodRoute } from './ordered-food.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(orderedFoodRoute)],
  declarations: [OrderedFoodComponent, OrderedFoodDetailComponent, OrderedFoodUpdateComponent, OrderedFoodDeleteDialogComponent],
  entryComponents: [OrderedFoodDeleteDialogComponent]
})
export class DeliverEthOrderedFoodModule {}
