import { Moment } from 'moment';
import { IOrderedFood } from 'app/shared/model/ordered-food.model';

export interface IOrder {
  id?: number;
  latitude?: string;
  longtude?: string;
  totalPrice?: string;
  date?: Moment;
  orderedFoods?: IOrderedFood[];
  telegramUserUserName?: string;
  telegramUserId?: number;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public latitude?: string,
    public longtude?: string,
    public totalPrice?: string,
    public date?: Moment,
    public orderedFoods?: IOrderedFood[],
    public telegramUserUserName?: string,
    public telegramUserId?: number
  ) {}
}
