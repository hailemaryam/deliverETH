import { IOrder } from 'app/shared/model/order.model';

export interface ITelegramUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  userName?: string;
  chatId?: string;
  phone?: string;
  conversationMetaData?: string;
  orders?: IOrder[];
}

export class TelegramUser implements ITelegramUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public userName?: string,
    public chatId?: string,
    public phone?: string,
    public conversationMetaData?: string,
    public orders?: IOrder[]
  ) {}
}
