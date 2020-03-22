import { IOrderedFood } from 'app/shared/model/ordered-food.model';

export interface IFood {
  id?: number;
  name?: string;
  description?: any;
  iconImageContentType?: string;
  iconImage?: any;
  price?: number;
  orderedFoods?: IOrderedFood[];
  restorantName?: string;
  restorantId?: number;
}

export class Food implements IFood {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public iconImageContentType?: string,
    public iconImage?: any,
    public price?: number,
    public orderedFoods?: IOrderedFood[],
    public restorantName?: string,
    public restorantId?: number
  ) {}
}
