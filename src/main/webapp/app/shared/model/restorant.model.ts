import { IFood } from 'app/shared/model/food.model';
import { ITelegramRestaurantUser } from 'app/shared/model/telegram-restaurant-user.model';

export interface IRestorant {
  id?: number;
  name?: string;
  userName?: string;
  description?: any;
  iconImageContentType?: string;
  iconImage?: any;
  latitude?: number;
  longtude?: number;
  availableOrderCap?: number;
  status?: boolean;
  foods?: IFood[];
  telegramRestaurantUsers?: ITelegramRestaurantUser[];
}

export class Restorant implements IRestorant {
  constructor(
    public id?: number,
    public name?: string,
    public userName?: string,
    public description?: any,
    public iconImageContentType?: string,
    public iconImage?: any,
    public latitude?: number,
    public longtude?: number,
    public availableOrderCap?: number,
    public status?: boolean,
    public foods?: IFood[],
    public telegramRestaurantUsers?: ITelegramRestaurantUser[]
  ) {
    this.status = this.status || false;
  }
}
