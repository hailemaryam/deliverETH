import { IFood } from 'app/shared/model/food.model';

export interface IRestorant {
  id?: number;
  name?: string;
  description?: any;
  iconImageContentType?: string;
  iconImage?: any;
  latitude?: string;
  longtude?: string;
  foods?: IFood[];
}

export class Restorant implements IRestorant {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public iconImageContentType?: string,
    public iconImage?: any,
    public latitude?: string,
    public longtude?: string,
    public foods?: IFood[]
  ) {}
}
