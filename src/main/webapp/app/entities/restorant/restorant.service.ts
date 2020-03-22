import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRestorant } from 'app/shared/model/restorant.model';

type EntityResponseType = HttpResponse<IRestorant>;
type EntityArrayResponseType = HttpResponse<IRestorant[]>;

@Injectable({ providedIn: 'root' })
export class RestorantService {
  public resourceUrl = SERVER_API_URL + 'api/restorants';

  constructor(protected http: HttpClient) {}

  create(restorant: IRestorant): Observable<EntityResponseType> {
    return this.http.post<IRestorant>(this.resourceUrl, restorant, { observe: 'response' });
  }

  update(restorant: IRestorant): Observable<EntityResponseType> {
    return this.http.put<IRestorant>(this.resourceUrl, restorant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestorant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestorant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
