import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderedFood } from 'app/shared/model/ordered-food.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { OrderedFoodService } from './ordered-food.service';
import { OrderedFoodDeleteDialogComponent } from './ordered-food-delete-dialog.component';

@Component({
  selector: 'jhi-ordered-food',
  templateUrl: './ordered-food.component.html'
})
export class OrderedFoodComponent implements OnInit, OnDestroy {
  orderedFoods?: IOrderedFood[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected orderedFoodService: OrderedFoodService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.orderedFoodService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IOrderedFood[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInOrderedFoods();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOrderedFood): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOrderedFoods(): void {
    this.eventSubscriber = this.eventManager.subscribe('orderedFoodListModification', () => this.loadPage());
  }

  delete(orderedFood: IOrderedFood): void {
    const modalRef = this.modalService.open(OrderedFoodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderedFood = orderedFood;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IOrderedFood[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/ordered-food'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.orderedFoods = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
