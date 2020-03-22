import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITelegramUser, TelegramUser } from 'app/shared/model/telegram-user.model';
import { TelegramUserService } from './telegram-user.service';
import { TelegramUserComponent } from './telegram-user.component';
import { TelegramUserDetailComponent } from './telegram-user-detail.component';
import { TelegramUserUpdateComponent } from './telegram-user-update.component';

@Injectable({ providedIn: 'root' })
export class TelegramUserResolve implements Resolve<ITelegramUser> {
  constructor(private service: TelegramUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITelegramUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((telegramUser: HttpResponse<TelegramUser>) => {
          if (telegramUser.body) {
            return of(telegramUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TelegramUser());
  }
}

export const telegramUserRoute: Routes = [
  {
    path: '',
    component: TelegramUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TelegramUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TelegramUserDetailComponent,
    resolve: {
      telegramUser: TelegramUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TelegramUserUpdateComponent,
    resolve: {
      telegramUser: TelegramUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TelegramUserUpdateComponent,
    resolve: {
      telegramUser: TelegramUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TelegramUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
