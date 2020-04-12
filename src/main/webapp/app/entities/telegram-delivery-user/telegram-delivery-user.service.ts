import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITelegramDeliveryUser } from 'app/shared/model/telegram-delivery-user.model';

type EntityResponseType = HttpResponse<ITelegramDeliveryUser>;
type EntityArrayResponseType = HttpResponse<ITelegramDeliveryUser[]>;

@Injectable({ providedIn: 'root' })
export class TelegramDeliveryUserService {
  public resourceUrl = SERVER_API_URL + 'api/telegram-delivery-users';

  constructor(protected http: HttpClient) {}

  create(telegramDeliveryUser: ITelegramDeliveryUser): Observable<EntityResponseType> {
    return this.http.post<ITelegramDeliveryUser>(this.resourceUrl, telegramDeliveryUser, { observe: 'response' });
  }

  update(telegramDeliveryUser: ITelegramDeliveryUser): Observable<EntityResponseType> {
    return this.http.put<ITelegramDeliveryUser>(this.resourceUrl, telegramDeliveryUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITelegramDeliveryUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITelegramDeliveryUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
