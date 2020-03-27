import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IKeyValuPairHoler, KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';
import { KeyValuPairHolerService } from './key-valu-pair-holer.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-key-valu-pair-holer-update',
  templateUrl: './key-valu-pair-holer-update.component.html'
})
export class KeyValuPairHolerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    key: [],
    valueString: [],
    valueNumber: [],
    valueImage: [],
    valueImageContentType: [],
    valueBlob: [],
    valueBlobContentType: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected keyValuPairHolerService: KeyValuPairHolerService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyValuPairHoler }) => {
      this.updateForm(keyValuPairHoler);
    });
  }

  updateForm(keyValuPairHoler: IKeyValuPairHoler): void {
    this.editForm.patchValue({
      id: keyValuPairHoler.id,
      key: keyValuPairHoler.key,
      valueString: keyValuPairHoler.valueString,
      valueNumber: keyValuPairHoler.valueNumber,
      valueImage: keyValuPairHoler.valueImage,
      valueImageContentType: keyValuPairHoler.valueImageContentType,
      valueBlob: keyValuPairHoler.valueBlob,
      valueBlobContentType: keyValuPairHoler.valueBlobContentType
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
    const keyValuPairHoler = this.createFromForm();
    if (keyValuPairHoler.id !== undefined) {
      this.subscribeToSaveResponse(this.keyValuPairHolerService.update(keyValuPairHoler));
    } else {
      this.subscribeToSaveResponse(this.keyValuPairHolerService.create(keyValuPairHoler));
    }
  }

  private createFromForm(): IKeyValuPairHoler {
    return {
      ...new KeyValuPairHoler(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      valueString: this.editForm.get(['valueString'])!.value,
      valueNumber: this.editForm.get(['valueNumber'])!.value,
      valueImageContentType: this.editForm.get(['valueImageContentType'])!.value,
      valueImage: this.editForm.get(['valueImage'])!.value,
      valueBlobContentType: this.editForm.get(['valueBlobContentType'])!.value,
      valueBlob: this.editForm.get(['valueBlob'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKeyValuPairHoler>>): void {
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
