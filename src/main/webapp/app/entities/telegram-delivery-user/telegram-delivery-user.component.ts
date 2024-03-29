import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TelegramDeliveryUserService } from './telegram-delivery-user.service';
import { TelegramDeliveryUserDeleteDialogComponent } from './telegram-delivery-user-delete-dialog.component';

@Component({
  selector: 'jhi-telegram-delivery-user',
  templateUrl: './telegram-delivery-user.component.html'
})
export class TelegramDeliveryUserComponent implements OnInit, OnDestroy {
  telegramDeliveryUsers?: ITelegramDeliveryUser[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected telegramDeliveryUserService: TelegramDeliveryUserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.telegramDeliveryUserService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ITelegramDeliveryUser[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInTelegramDeliveryUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITelegramDeliveryUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTelegramDeliveryUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('telegramDeliveryUserListModification', () => this.loadPage());
  }

  delete(telegramDeliveryUser: ITelegramDeliveryUser): void {
    const modalRef = this.modalService.open(TelegramDeliveryUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.telegramDeliveryUser = telegramDeliveryUser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITelegramDeliveryUser[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/telegram-delivery-user'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.telegramDeliveryUsers = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
