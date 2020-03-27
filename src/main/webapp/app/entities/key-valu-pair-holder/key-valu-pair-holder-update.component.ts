import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IKeyValuPairHolder, KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';
import { KeyValuPairHolderService } from './key-valu-pair-holder.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-key-valu-pair-holder-update',
  templateUrl: './key-valu-pair-holder-update.component.html'
})
export class KeyValuPairHolderUpdateComponent implements OnInit {
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
    protected keyValuPairHolderService: KeyValuPairHolderService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyValuPairHolder }) => {
      this.updateForm(keyValuPairHolder);
    });
  }

  updateForm(keyValuPairHolder: IKeyValuPairHolder): void {
    this.editForm.patchValue({
      id: keyValuPairHolder.id,
      key: keyValuPairHolder.key,
      valueString: keyValuPairHolder.valueString,
      valueNumber: keyValuPairHolder.valueNumber,
      valueImage: keyValuPairHolder.valueImage,
      valueImageContentType: keyValuPairHolder.valueImageContentType,
      valueBlob: keyValuPairHolder.valueBlob,
      valueBlobContentType: keyValuPairHolder.valueBlobContentType
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
    const keyValuPairHolder = this.createFromForm();
    if (keyValuPairHolder.id !== undefined) {
      this.subscribeToSaveResponse(this.keyValuPairHolderService.update(keyValuPairHolder));
    } else {
      this.subscribeToSaveResponse(this.keyValuPairHolderService.create(keyValuPairHolder));
    }
  }

  private createFromForm(): IKeyValuPairHolder {
    return {
      ...new KeyValuPairHolder(),
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKeyValuPairHolder>>): void {
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
