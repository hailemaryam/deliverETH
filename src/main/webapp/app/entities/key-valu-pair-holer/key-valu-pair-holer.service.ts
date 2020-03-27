import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IKeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

type EntityResponseType = HttpResponse<IKeyValuPairHoler>;
type EntityArrayResponseType = HttpResponse<IKeyValuPairHoler[]>;

@Injectable({ providedIn: 'root' })
export class KeyValuPairHolerService {
  public resourceUrl = SERVER_API_URL + 'api/key-valu-pair-holers';

  constructor(protected http: HttpClient) {}

  create(keyValuPairHoler: IKeyValuPairHoler): Observable<EntityResponseType> {
    return this.http.post<IKeyValuPairHoler>(this.resourceUrl, keyValuPairHoler, { observe: 'response' });
  }

  update(keyValuPairHoler: IKeyValuPairHoler): Observable<EntityResponseType> {
    return this.http.put<IKeyValuPairHoler>(this.resourceUrl, keyValuPairHoler, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKeyValuPairHoler>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKeyValuPairHoler[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
