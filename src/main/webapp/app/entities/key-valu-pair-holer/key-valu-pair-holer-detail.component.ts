import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IKeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

@Component({
  selector: 'jhi-key-valu-pair-holer-detail',
  templateUrl: './key-valu-pair-holer-detail.component.html'
})
export class KeyValuPairHolerDetailComponent implements OnInit {
  keyValuPairHoler: IKeyValuPairHoler | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ keyValuPairHoler }) => (this.keyValuPairHoler = keyValuPairHoler));
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
