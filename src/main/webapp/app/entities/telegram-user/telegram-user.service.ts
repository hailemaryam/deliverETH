import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITelegramUser } from 'app/shared/model/telegram-user.model';

type EntityResponseType = HttpResponse<ITelegramUser>;
type EntityArrayResponseType = HttpResponse<ITelegramUser[]>;

@Injectable({ providedIn: 'root' })
export class TelegramUserService {
  public resourceUrl = SERVER_API_URL + 'api/telegram-users';

  constructor(protected http: HttpClient) {}

  create(telegramUser: ITelegramUser): Observable<EntityResponseType> {
    return this.http.post<ITelegramUser>(this.resourceUrl, telegramUser, { observe: 'response' });
  }

  update(telegramUser: ITelegramUser): Observable<EntityResponseType> {
    return this.http.put<ITelegramUser>(this.resourceUrl, telegramUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITelegramUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITelegramUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
