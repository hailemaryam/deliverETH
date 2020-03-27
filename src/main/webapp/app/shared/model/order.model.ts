import { Moment } from 'moment';
import { IOrderedFood } from 'app/shared/model/ordered-food.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  latitude?: string;
  longtude?: string;
  locationDescription?: any;
  totalPrice?: string;
  date?: Moment;
  additionalNote?: any;
  orderStatus?: OrderStatus;
  orderedFoods?: IOrderedFood[];
  telegramUserUserName?: string;
  telegramUserId?: number;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public latitude?: string,
    public longtude?: string,
    public locationDescription?: any,
    public totalPrice?: string,
    public date?: Moment,
    public additionalNote?: any,
    public orderStatus?: OrderStatus,
    public orderedFoods?: IOrderedFood[],
    public telegramUserUserName?: string,
    public telegramUserId?: number
  ) {}
}
