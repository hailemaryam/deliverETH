import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITelegramDeliveryUser, TelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';
import { TelegramDeliveryUserService } from './telegram-delivery-user.service';
import { TelegramDeliveryUserComponent } from './telegram-delivery-user.component';
import { TelegramDeliveryUserDetailComponent } from './telegram-delivery-user-detail.component';
import { TelegramDeliveryUserUpdateComponent } from './telegram-delivery-user-update.component';

@Injectable({ providedIn: 'root' })
export class TelegramDeliveryUserResolve implements Resolve<ITelegramDeliveryUser> {
  constructor(private service: TelegramDeliveryUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITelegramDeliveryUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((telegramDeliveryUser: HttpResponse<TelegramDeliveryUser>) => {
          if (telegramDeliveryUser.body) {
            return of(telegramDeliveryUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TelegramDeliveryUser());
  }
}

export const telegramDeliveryUserRoute: Routes = [
  {
    path: '',
    component: TelegramDeliveryUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TelegramDeliveryUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TelegramDeliveryUserDetailComponent,
    resolve: {
      telegramDeliveryUser: TelegramDeliveryUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramDeliveryUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TelegramDeliveryUserUpdateComponent,
    resolve: {
      telegramDeliveryUser: TelegramDeliveryUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramDeliveryUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TelegramDeliveryUserUpdateComponent,
    resolve: {
      telegramDeliveryUser: TelegramDeliveryUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramDeliveryUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
