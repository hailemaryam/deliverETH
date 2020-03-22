import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrderedFood, OrderedFood } from 'app/shared/model/ordered-food.model';
import { OrderedFoodService } from './ordered-food.service';
import { OrderedFoodComponent } from './ordered-food.component';
import { OrderedFoodDetailComponent } from './ordered-food-detail.component';
import { OrderedFoodUpdateComponent } from './ordered-food-update.component';

@Injectable({ providedIn: 'root' })
export class OrderedFoodResolve implements Resolve<IOrderedFood> {
  constructor(private service: OrderedFoodService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderedFood> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((orderedFood: HttpResponse<OrderedFood>) => {
          if (orderedFood.body) {
            return of(orderedFood.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderedFood());
  }
}

export const orderedFoodRoute: Routes = [
  {
    path: '',
    component: OrderedFoodComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'OrderedFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OrderedFoodDetailComponent,
    resolve: {
      orderedFood: OrderedFoodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'OrderedFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OrderedFoodUpdateComponent,
    resolve: {
      orderedFood: OrderedFoodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'OrderedFoods'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OrderedFoodUpdateComponent,
    resolve: {
      orderedFood: OrderedFoodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'OrderedFoods'
    },
    canActivate: [UserRouteAccessService]
  }
];
