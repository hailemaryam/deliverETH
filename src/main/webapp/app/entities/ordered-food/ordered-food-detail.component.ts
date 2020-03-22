import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderedFood } from 'app/shared/model/ordered-food.model';

@Component({
  selector: 'jhi-ordered-food-detail',
  templateUrl: './ordered-food-detail.component.html'
})
export class OrderedFoodDetailComponent implements OnInit {
  orderedFood: IOrderedFood | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderedFood }) => (this.orderedFood = orderedFood));
  }

  previousState(): void {
    window.history.back();
  }
}
