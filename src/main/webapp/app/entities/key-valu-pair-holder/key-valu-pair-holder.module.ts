import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DeliverEthSharedModule } from 'app/shared/shared.module';
import { KeyValuPairHolderComponent } from './key-valu-pair-holder.component';
import { KeyValuPairHolderDetailComponent } from './key-valu-pair-holder-detail.component';
import { KeyValuPairHolderUpdateComponent } from './key-valu-pair-holder-update.component';
import { KeyValuPairHolderDeleteDialogComponent } from './key-valu-pair-holder-delete-dialog.component';
import { keyValuPairHolderRoute } from './key-valu-pair-holder.route';

@NgModule({
  imports: [DeliverEthSharedModule, RouterModule.forChild(keyValuPairHolderRoute)],
  declarations: [
    KeyValuPairHolderComponent,
    KeyValuPairHolderDetailComponent,
    KeyValuPairHolderUpdateComponent,
    KeyValuPairHolderDeleteDialogComponent
  ],
  entryComponents: [KeyValuPairHolderDeleteDialogComponent]
})
export class DeliverEthKeyValuPairHolderModule {}
