import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IKeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

@Component({
  selector: 'jhi-key-valu-pair-holder-detail',
  templateUrl: './key-valu-pair-holder-detail.component.html'
})
export class KeyValuPairHolderDetailComponent implements OnInit {
  keyValuPairHolder: IKeyValuPairHolder | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyValuPairHolder }) => (this.keyValuPairHolder = keyValuPairHolder));
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
