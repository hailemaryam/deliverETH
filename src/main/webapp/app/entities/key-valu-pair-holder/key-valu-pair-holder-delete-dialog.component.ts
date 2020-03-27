import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';
import { KeyValuPairHolderService } from './key-valu-pair-holder.service';

@Component({
  templateUrl: './key-valu-pair-holder-delete-dialog.component.html'
})
export class KeyValuPairHolderDeleteDialogComponent {
  keyValuPairHolder?: IKeyValuPairHolder;

  constructor(
    protected keyValuPairHolderService: KeyValuPairHolderService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.keyValuPairHolderService.delete(id).subscribe(() => {
      this.eventManager.broadcast('keyValuPairHolderListModification');
      this.activeModal.close();
    });
  }
}
