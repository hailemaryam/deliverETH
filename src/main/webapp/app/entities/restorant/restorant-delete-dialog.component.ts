import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRestorant } from 'app/shared/model/restorant.model';
import { RestorantService } from './restorant.service';

@Component({
  templateUrl: './restorant-delete-dialog.component.html'
})
export class RestorantDeleteDialogComponent {
  restorant?: IRestorant;

  constructor(protected restorantService: RestorantService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restorantService.delete(id).subscribe(() => {
      this.eventManager.broadcast('restorantListModification');
      this.activeModal.close();
    });
  }
}
