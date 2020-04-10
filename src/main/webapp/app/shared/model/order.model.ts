import { Moment } from 'moment';
import { IOrderedFood } from 'app/shared/model/ordered-food.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  latitude?: number;
  longtude?: number;
  locationDescription?: any;
  totalPrice?: number;
  transportationFee?: number;
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
    public latitude?: number,
    public longtude?: number,
    public locationDescription?: any,
    public totalPrice?: number,
    public transportationFee?: number,
    public date?: Moment,
    public additionalNote?: any,
    public orderStatus?: OrderStatus,
    public orderedFoods?: IOrderedFood[],
    public telegramUserUserName?: string,
    public telegramUserId?: number
  ) {}
}
