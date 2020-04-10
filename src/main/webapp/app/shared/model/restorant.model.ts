import { IFood } from 'app/shared/model/food.model';

export interface IRestorant {
  id?: number;
  name?: string;
  userName?: string;
  description?: any;
  iconImageContentType?: string;
  iconImage?: any;
  latitude?: number;
  longtude?: number;
  foods?: IFood[];
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
    public foods?: IFood[]
  ) {}
}
