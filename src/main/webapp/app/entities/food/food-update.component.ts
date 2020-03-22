import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFood, Food } from 'app/shared/model/food.model';
import { FoodService } from './food.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IRestorant } from 'app/shared/model/restorant.model';
import { RestorantService } from 'app/entities/restorant/restorant.service';

@Component({
  selector: 'jhi-food-update',
  templateUrl: './food-update.component.html'
})
export class FoodUpdateComponent implements OnInit {
  isSaving = false;
  restorants: IRestorant[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    iconImage: [],
    iconImageContentType: [],
    price: [],
    restorantId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected foodService: FoodService,
    protected restorantService: RestorantService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.updateForm(food);

      this.restorantService.query().subscribe((res: HttpResponse<IRestorant[]>) => (this.restorants = res.body || []));
    });
  }

  updateForm(food: IFood): void {
    this.editForm.patchValue({
      id: food.id,
      name: food.name,
      description: food.description,
      iconImage: food.iconImage,
      iconImageContentType: food.iconImageContentType,
      price: food.price,
      restorantId: food.restorantId
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const food = this.createFromForm();
    if (food.id !== undefined) {
      this.subscribeToSaveResponse(this.foodService.update(food));
    } else {
      this.subscribeToSaveResponse(this.foodService.create(food));
    }
  }

  private createFromForm(): IFood {
    return {
      ...new Food(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      iconImageContentType: this.editForm.get(['iconImageContentType'])!.value,
      iconImage: this.editForm.get(['iconImage'])!.value,
      price: this.editForm.get(['price'])!.value,
      restorantId: this.editForm.get(['restorantId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFood>>): void {
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

  trackById(index: number, item: IRestorant): any {
    return item.id;
  }
}
