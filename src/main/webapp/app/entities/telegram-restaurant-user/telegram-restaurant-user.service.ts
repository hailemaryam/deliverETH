import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

type EntityResponseType = HttpResponse<ITelegramRestaurantUser>;
type EntityArrayResponseType = HttpResponse<ITelegramRestaurantUser[]>;

@Injectable({ providedIn: 'root' })
export class TelegramRestaurantUserService {
  public resourceUrl = SERVER_API_URL + 'api/telegram-restaurant-users';

  constructor(protected http: HttpClient) {}

  create(telegramRestaurantUser: ITelegramRestaurantUser): Observable<EntityResponseType> {
    return this.http.post<ITelegramRestaurantUser>(this.resourceUrl, telegramRestaurantUser, { observe: 'response' });
  }

  update(telegramRestaurantUser: ITelegramRestaurantUser): Observable<EntityResponseType> {
    return this.http.put<ITelegramRestaurantUser>(this.resourceUrl, telegramRestaurantUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITelegramRestaurantUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITelegramRestaurantUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
