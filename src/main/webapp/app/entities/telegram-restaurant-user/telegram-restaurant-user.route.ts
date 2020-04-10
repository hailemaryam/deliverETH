import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITelegramRestaurantUser, TelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';
import { TelegramRestaurantUserService } from './telegram-restaurant-user.service';
import { TelegramRestaurantUserComponent } from './telegram-restaurant-user.component';
import { TelegramRestaurantUserDetailComponent } from './telegram-restaurant-user-detail.component';
import { TelegramRestaurantUserUpdateComponent } from './telegram-restaurant-user-update.component';

@Injectable({ providedIn: 'root' })
export class TelegramRestaurantUserResolve implements Resolve<ITelegramRestaurantUser> {
  constructor(private service: TelegramRestaurantUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITelegramRestaurantUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((telegramRestaurantUser: HttpResponse<TelegramRestaurantUser>) => {
          if (telegramRestaurantUser.body) {
            return of(telegramRestaurantUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TelegramRestaurantUser());
  }
}

export const telegramRestaurantUserRoute: Routes = [
  {
    path: '',
    component: TelegramRestaurantUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TelegramRestaurantUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TelegramRestaurantUserDetailComponent,
    resolve: {
      telegramRestaurantUser: TelegramRestaurantUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramRestaurantUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TelegramRestaurantUserUpdateComponent,
    resolve: {
      telegramRestaurantUser: TelegramRestaurantUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramRestaurantUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TelegramRestaurantUserUpdateComponent,
    resolve: {
      telegramRestaurantUser: TelegramRestaurantUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramRestaurantUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
