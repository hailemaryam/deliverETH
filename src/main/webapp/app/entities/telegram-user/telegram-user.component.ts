import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITelegramUser } from 'app/shared/model/telegram-user.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TelegramUserService } from './telegram-user.service';
import { TelegramUserDeleteDialogComponent } from './telegram-user-delete-dialog.component';

@Component({
  selector: 'jhi-telegram-user',
  templateUrl: './telegram-user.component.html'
})
export class TelegramUserComponent implements OnInit, OnDestroy {
  telegramUsers?: ITelegramUser[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected telegramUserService: TelegramUserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.telegramUserService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ITelegramUser[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInTelegramUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITelegramUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTelegramUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('telegramUserListModification', () => this.loadPage());
  }

  delete(telegramUser: ITelegramUser): void {
    const modalRef = this.modalService.open(TelegramUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.telegramUser = telegramUser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITelegramUser[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/telegram-user'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.telegramUsers = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
