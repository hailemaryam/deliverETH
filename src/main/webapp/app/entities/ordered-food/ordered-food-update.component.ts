import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IOrderedFood, OrderedFood } from 'app/shared/model/ordered-food.model';
import { OrderedFoodService } from './ordered-food.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IFood } from 'app/shared/model/food.model';
import { FoodService } from 'app/entities/food/food.service';
import { IOrder } from 'app/shared/model/order.model';
import { OrderService } from 'app/entities/order/order.service';

type SelectableEntity = IFood | IOrder;

@Component({
  selector: 'jhi-ordered-food-update',
  templateUrl: './ordered-food-update.component.html'
})
export class OrderedFoodUpdateComponent implements OnInit {
  isSaving = false;
  foods: IFood[] = [];
  orders: IOrder[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [],
    additionalNote: [],
    foodId: [],
    orderId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected orderedFoodService: OrderedFoodService,
    protected foodService: FoodService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderedFood }) => {
      this.updateForm(orderedFood);

      this.foodService.query().subscribe((res: HttpResponse<IFood[]>) => (this.foods = res.body || []));

      this.orderService.query().subscribe((res: HttpResponse<IOrder[]>) => (this.orders = res.body || []));
    });
  }

  updateForm(orderedFood: IOrderedFood): void {
    this.editForm.patchValue({
      id: orderedFood.id,
      quantity: orderedFood.quantity,
      additionalNote: orderedFood.additionalNote,
      foodId: orderedFood.foodId,
      orderId: orderedFood.orderId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('deliverEthApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderedFood = this.createFromForm();
    if (orderedFood.id !== undefined) {
      this.subscribeToSaveResponse(this.orderedFoodService.update(orderedFood));
    } else {
      this.subscribeToSaveResponse(this.orderedFoodService.create(orderedFood));
    }
  }

  private createFromForm(): IOrderedFood {
    return {
      ...new OrderedFood(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      additionalNote: this.editForm.get(['additionalNote'])!.value,
      foodId: this.editForm.get(['foodId'])!.value,
      orderId: this.editForm.get(['orderId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderedFood>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
