import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TelegramRestaurantUserService } from './telegram-restaurant-user.service';
import { TelegramRestaurantUserDeleteDialogComponent } from './telegram-restaurant-user-delete-dialog.component';

@Component({
  selector: 'jhi-telegram-restaurant-user',
  templateUrl: './telegram-restaurant-user.component.html'
})
export class TelegramRestaurantUserComponent implements OnInit, OnDestroy {
  telegramRestaurantUsers?: ITelegramRestaurantUser[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected telegramRestaurantUserService: TelegramRestaurantUserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.telegramRestaurantUserService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ITelegramRestaurantUser[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInTelegramRestaurantUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITelegramRestaurantUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTelegramRestaurantUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('telegramRestaurantUserListModification', () => this.loadPage());
  }

  delete(telegramRestaurantUser: ITelegramRestaurantUser): void {
    const modalRef = this.modalService.open(TelegramRestaurantUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.telegramRestaurantUser = telegramRestaurantUser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITelegramRestaurantUser[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/telegram-restaurant-user'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.telegramRestaurantUsers = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
