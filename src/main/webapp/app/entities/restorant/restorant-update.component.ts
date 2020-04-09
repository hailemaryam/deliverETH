import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IRestorant, Restorant } from 'app/shared/model/restorant.model';
import { RestorantService } from './restorant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-restorant-update',
  templateUrl: './restorant-update.component.html'
})
export class RestorantUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    userName: [null, [Validators.pattern('^[-a-zA-Z0-9@\\.+_]+$')]],
    description: [],
    iconImage: [],
    iconImageContentType: [],
    latitude: [],
    longtude: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected restorantService: RestorantService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restorant }) => {
      this.updateForm(restorant);
    });
  }

  updateForm(restorant: IRestorant): void {
    this.editForm.patchValue({
      id: restorant.id,
      name: restorant.name,
      userName: restorant.userName,
      description: restorant.description,
      iconImage: restorant.iconImage,
      iconImageContentType: restorant.iconImageContentType,
      latitude: restorant.latitude,
      longtude: restorant.longtude
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
    const restorant = this.createFromForm();
    if (restorant.id !== undefined) {
      this.subscribeToSaveResponse(this.restorantService.update(restorant));
    } else {
      this.subscribeToSaveResponse(this.restorantService.create(restorant));
    }
  }

  private createFromForm(): IRestorant {
    return {
      ...new Restorant(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      description: this.editForm.get(['description'])!.value,
      iconImageContentType: this.editForm.get(['iconImageContentType'])!.value,
      iconImage: this.editForm.get(['iconImage'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longtude: this.editForm.get(['longtude'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestorant>>): void {
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
}
