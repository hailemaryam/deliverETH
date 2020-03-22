import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrderedFood } from 'app/shared/model/ordered-food.model';
import { OrderedFoodService } from './ordered-food.service';

@Component({
  templateUrl: './ordered-food-delete-dialog.component.html'
})
export class OrderedFoodDeleteDialogComponent {
  orderedFood?: IOrderedFood;

  constructor(
    protected orderedFoodService: OrderedFoodService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderedFoodService.delete(id).subscribe(() => {
      this.eventManager.broadcast('orderedFoodListModification');
      this.activeModal.close();
    });
  }
}
