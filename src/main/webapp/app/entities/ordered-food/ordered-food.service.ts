import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrderedFood } from 'app/shared/model/ordered-food.model';

type EntityResponseType = HttpResponse<IOrderedFood>;
type EntityArrayResponseType = HttpResponse<IOrderedFood[]>;

@Injectable({ providedIn: 'root' })
export class OrderedFoodService {
  public resourceUrl = SERVER_API_URL + 'api/ordered-foods';

  constructor(protected http: HttpClient) {}

  create(orderedFood: IOrderedFood): Observable<EntityResponseType> {
    return this.http.post<IOrderedFood>(this.resourceUrl, orderedFood, { observe: 'response' });
  }

  update(orderedFood: IOrderedFood): Observable<EntityResponseType> {
    return this.http.put<IOrderedFood>(this.resourceUrl, orderedFood, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderedFood>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderedFood[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
