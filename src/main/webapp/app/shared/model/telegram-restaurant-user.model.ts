import { IRestorant } from 'app/shared/model/restorant.model';

export interface ITelegramRestaurantUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  userName?: string;
  userId?: number;
  chatId?: string;
  phone?: string;
  conversationMetaData?: string;
  loadedPage?: number;
  restorants?: IRestorant[];
}

export class TelegramRestaurantUser implements ITelegramRestaurantUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public userName?: string,
    public userId?: number,
    public chatId?: string,
    public phone?: string,
    public conversationMetaData?: string,
    public loadedPage?: number,
    public restorants?: IRestorant[]
  ) {}
}
