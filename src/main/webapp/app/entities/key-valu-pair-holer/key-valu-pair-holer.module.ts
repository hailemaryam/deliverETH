import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { KeyValuPairHolerComponent } from './key-valu-pair-holer.component';
import { KeyValuPairHolerDetailComponent } from './key-valu-pair-holer-detail.component';
import { KeyValuPairHolerUpdateComponent } from './key-valu-pair-holer-update.component';
import { KeyValuPairHolerDeleteDialogComponent } from './key-valu-pair-holer-delete-dialog.component';
import { keyValuPairHolerRoute } from './key-valu-pair-holer.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(keyValuPairHolerRoute)],
  declarations: [
    KeyValuPairHolerComponent,
    KeyValuPairHolerDetailComponent,
    KeyValuPairHolerUpdateComponent,
    KeyValuPairHolerDeleteDialogComponent
  ],
  entryComponents: [KeyValuPairHolerDeleteDialogComponent]
})
export class DeliverEthKeyValuPairHolerModule {}
