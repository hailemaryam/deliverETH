import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRestorant, Restorant } from 'app/shared/model/restorant.model';
import { RestorantService } from './restorant.service';
import { RestorantComponent } from './restorant.component';
import { RestorantDetailComponent } from './restorant-detail.component';
import { RestorantUpdateComponent } from './restorant-update.component';

@Injectable({ providedIn: 'root' })
export class RestorantResolve implements Resolve<IRestorant> {
  constructor(private service: RestorantService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestorant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((restorant: HttpResponse<Restorant>) => {
          if (restorant.body) {
            return of(restorant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Restorant());
  }
}

export const restorantRoute: Routes = [
  {
    path: '',
    component: RestorantComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Restorants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RestorantDetailComponent,
    resolve: {
      restorant: RestorantResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Restorants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RestorantUpdateComponent,
    resolve: {
      restorant: RestorantResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Restorants'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RestorantUpdateComponent,
    resolve: {
      restorant: RestorantResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Restorants'
    },
    canActivate: [UserRouteAccessService]
  }
];
