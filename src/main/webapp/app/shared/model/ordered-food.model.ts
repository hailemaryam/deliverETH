export interface IOrderedFood {
  id?: number;
  quantity?: number;
  foodName?: string;
  foodId?: number;
  orderDate?: string;
  orderId?: number;
}

export class OrderedFood implements IOrderedFood {
  constructor(
    public id?: number,
    public quantity?: number,
    public foodName?: string,
    public foodId?: number,
    public orderDate?: string,
    public orderId?: number
  ) {}
}
