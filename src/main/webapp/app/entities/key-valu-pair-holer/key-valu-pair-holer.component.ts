import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IKeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { KeyValuPairHolerService } from './key-valu-pair-holer.service';
import { KeyValuPairHolerDeleteDialogComponent } from './key-valu-pair-holer-delete-dialog.component';

@Component({
  selector: 'jhi-key-valu-pair-holer',
  templateUrl: './key-valu-pair-holer.component.html'
})
export class KeyValuPairHolerComponent implements OnInit, OnDestroy {
  keyValuPairHolers?: IKeyValuPairHoler[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected keyValuPairHolerService: KeyValuPairHolerService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.keyValuPairHolerService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IKeyValuPairHoler[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInKeyValuPairHolers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IKeyValuPairHoler): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInKeyValuPairHolers(): void {
    this.eventSubscriber = this.eventManager.subscribe('keyValuPairHolerListModification', () => this.loadPage());
  }

  delete(keyValuPairHoler: IKeyValuPairHoler): void {
    const modalRef = this.modalService.open(KeyValuPairHolerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.keyValuPairHoler = keyValuPairHoler;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IKeyValuPairHoler[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/key-valu-pair-holer'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.keyValuPairHolers = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
