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
  restaurantPaymentStaus?: boolean;
  transportPaymentStatus?: boolean;
  telegramUserPaymentStatus?: boolean;
  orderedFoods?: IOrderedFood[];
  telegramUserUserName?: string;
  telegramUserId?: number;
  telegramDeliveryUserUserName?: string;
  telegramDeliveryUserId?: number;
  telegramRestaurantUserUserName?: string;
  telegramRestaurantUserId?: number;
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
    public restaurantPaymentStaus?: boolean,
    public transportPaymentStatus?: boolean,
    public telegramUserPaymentStatus?: boolean,
    public orderedFoods?: IOrderedFood[],
    public telegramUserUserName?: string,
    public telegramUserId?: number,
    public telegramDeliveryUserUserName?: string,
    public telegramDeliveryUserId?: number,
    public telegramRestaurantUserUserName?: string,
    public telegramRestaurantUserId?: number
  ) {
    this.restaurantPaymentStaus = this.restaurantPaymentStaus || false;
    this.transportPaymentStatus = this.transportPaymentStatus || false;
    this.telegramUserPaymentStatus = this.telegramUserPaymentStatus || false;
  }
}
