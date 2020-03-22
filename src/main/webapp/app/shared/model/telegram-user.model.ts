import { IOrder } from 'app/shared/model/order.model';

export interface ITelegramUser {
  id?: number;
  userName?: string;
  chatId?: string;
  phone?: string;
  orders?: IOrder[];
}

export class TelegramUser implements ITelegramUser {
  constructor(public id?: number, public userName?: string, public chatId?: string, public phone?: string, public orders?: IOrder[]) {}
}
