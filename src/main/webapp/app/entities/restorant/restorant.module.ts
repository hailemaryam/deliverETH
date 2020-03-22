import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { RestorantComponent } from './restorant.component';
import { RestorantDetailComponent } from './restorant-detail.component';
import { RestorantUpdateComponent } from './restorant-update.component';
import { RestorantDeleteDialogComponent } from './restorant-delete-dialog.component';
import { restorantRoute } from './restorant.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(restorantRoute)],
  declarations: [RestorantComponent, RestorantDetailComponent, RestorantUpdateComponent, RestorantDeleteDialogComponent],
  entryComponents: [RestorantDeleteDialogComponent]
})
export class DeliverEthRestorantModule {}
