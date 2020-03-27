import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKeyValuPairHolder, KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';
import { KeyValuPairHolderService } from './key-valu-pair-holder.service';
import { KeyValuPairHolderComponent } from './key-valu-pair-holder.component';
import { KeyValuPairHolderDetailComponent } from './key-valu-pair-holder-detail.component';
import { KeyValuPairHolderUpdateComponent } from './key-valu-pair-holder-update.component';

@Injectable({ providedIn: 'root' })
export class KeyValuPairHolderResolve implements Resolve<IKeyValuPairHolder> {
  constructor(private service: KeyValuPairHolderService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKeyValuPairHolder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((keyValuPairHolder: HttpResponse<KeyValuPairHolder>) => {
          if (keyValuPairHolder.body) {
            return of(keyValuPairHolder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new KeyValuPairHolder());
  }
}

export const keyValuPairHolderRoute: Routes = [
  {
    path: '',
    component: KeyValuPairHolderComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'KeyValuPairHolders'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: KeyValuPairHolderDetailComponent,
    resolve: {
      keyValuPairHolder: KeyValuPairHolderResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolders'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: KeyValuPairHolderUpdateComponent,
    resolve: {
      keyValuPairHolder: KeyValuPairHolderResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolders'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: KeyValuPairHolderUpdateComponent,
    resolve: {
      keyValuPairHolder: KeyValuPairHolderResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'KeyValuPairHolders'
    },
    canActivate: [UserRouteAccessService]
  }
];
