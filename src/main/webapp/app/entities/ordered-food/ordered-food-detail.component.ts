import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOrderedFood } from 'app/shared/model/ordered-food.model';

@Component({
  selector: 'jhi-ordered-food-detail',
  templateUrl: './ordered-food-detail.component.html'
})
export class OrderedFoodDetailComponent implements OnInit {
  orderedFood: IOrderedFood | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderedFood }) => (this.orderedFood = orderedFood));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
