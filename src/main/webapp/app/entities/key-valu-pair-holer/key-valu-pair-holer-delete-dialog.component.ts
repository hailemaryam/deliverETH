import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';
import { KeyValuPairHolerService } from './key-valu-pair-holer.service';

@Component({
  templateUrl: './key-valu-pair-holer-delete-dialog.component.html'
})
export class KeyValuPairHolerDeleteDialogComponent {
  keyValuPairHoler?: IKeyValuPairHoler;

  constructor(
    protected keyValuPairHolerService: KeyValuPairHolerService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.keyValuPairHolerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('keyValuPairHolerListModification');
      this.activeModal.close();
    });
  }
}
