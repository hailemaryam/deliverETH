import { IOrder } from 'app/shared/model/order.model';

export interface ITelegramDeliveryUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  userName?: string;
  chatId?: string;
  phone?: string;
  conversationMetaData?: string;
  loadedPage?: number;
  orders?: IOrder[];
}

export class TelegramDeliveryUser implements ITelegramDeliveryUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public userName?: string,
    public chatId?: string,
    public phone?: string,
    public conversationMetaData?: string,
    public loadedPage?: number,
    public orders?: IOrder[]
  ) {}
}
