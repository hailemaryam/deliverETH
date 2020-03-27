import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKeyValuPairHoler, KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';
import { KeyValuPairHolerService } from './key-valu-pair-holer.service';
import { KeyValuPairHolerComponent } from './key-valu-pair-holer.component';
import { KeyValuPairHolerDetailComponent } from './key-valu-pair-holer-detail.component';
import { KeyValuPairHolerUpdateComponent } from './key-valu-pair-holer-update.component';

@Injectable({ providedIn: 'root' })
export class KeyValuPairHolerResolve implements Resolve<IKeyValuPairHoler> {
  constructor(private service: KeyValuPairHolerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKeyValuPairHoler> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((keyValuPairHoler: HttpResponse<KeyValuPairHoler>) => {
          if (keyValuPairHoler.body) {
            return of(keyValuPairHoler.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new KeyValuPairHoler());
  }
}

export const keyValuPairHolerRoute: Routes = [
  {
    path: '',
    component: KeyValuPairHolerComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'KeyValuPairHolers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: KeyValuPairHolerDetailComponent,
    resolve: {
      keyValuPairHoler: KeyValuPairHolerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: KeyValuPairHolerUpdateComponent,
    resolve: {
      keyValuPairHoler: KeyValuPairHolerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: KeyValuPairHolerUpdateComponent,
    resolve: {
      keyValuPairHoler: KeyValuPairHolerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolers'
    },
    canActivate: [UserRouteAccessService]
  }
];
