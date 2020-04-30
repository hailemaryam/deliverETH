import { IOrder } from 'app/shared/model/order.model';
import { IRestorant } from 'app/shared/model/restorant.model';

export interface ITelegramDeliveryUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  userName?: string;
  userId?: number;
  chatId?: string;
  phone?: string;
  conversationMetaData?: string;
  loadedPage?: number;
  status?: boolean;
  currentBalance?: number;
  currentLatitude?: number;
  currentLongitude?: number;
  orders?: IOrder[];
  restorants?: IRestorant[];
}

export class TelegramDeliveryUser implements ITelegramDeliveryUser {
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
    public status?: boolean,
    public currentBalance?: number,
    public currentLatitude?: number,
    public currentLongitude?: number,
    public orders?: IOrder[],
    public restorants?: IRestorant[]
  ) {
    this.status = this.status || false;
  }
}
