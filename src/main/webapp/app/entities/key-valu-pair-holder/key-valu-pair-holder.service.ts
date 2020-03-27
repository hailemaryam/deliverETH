import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IKeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

type EntityResponseType = HttpResponse<IKeyValuPairHolder>;
type EntityArrayResponseType = HttpResponse<IKeyValuPairHolder[]>;

@Injectable({ providedIn: 'root' })
export class KeyValuPairHolderService {
  public resourceUrl = SERVER_API_URL + 'api/key-valu-pair-holders';

  constructor(protected http: HttpClient) {}

  create(keyValuPairHolder: IKeyValuPairHolder): Observable<EntityResponseType> {
    return this.http.post<IKeyValuPairHolder>(this.resourceUrl, keyValuPairHolder, { observe: 'response' });
  }

  update(keyValuPairHolder: IKeyValuPairHolder): Observable<EntityResponseType> {
    return this.http.put<IKeyValuPairHolder>(this.resourceUrl, keyValuPairHolder, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKeyValuPairHolder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKeyValuPairHolder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
