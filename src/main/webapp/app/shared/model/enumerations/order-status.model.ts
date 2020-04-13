export const enum OrderStatus {
  STARTED = 'STARTED',
  ORDERED = 'ORDERED',
  ACCEPTED_BY_RESTAURANT = 'ACCEPTED_BY_RESTAURANT',
  READY_FOR_DELIVERY = 'READY_FOR_DELIVERY',
  ACCEPTED_BY_DRIVER = 'ACCEPTED_BY_DRIVER',
  DELIVERED = 'DELIVERED',
  CANCELED_BY_RESTAURANT = 'CANCELED_BY_RESTAURANT',
  CANCELED_BY_USER = 'CANCELED_BY_USER',
  EXPIRED_AND_CANCELED_BY_SYSTEM = 'EXPIRED_AND_CANCELED_BY_SYSTEM',
  DELIVERED_AND_REMOVED = 'DELIVERED_AND_REMOVED',
  CANCELED_BY_RESTAURANT_AND_REMOVED = 'CANCELED_BY_RESTAURANT_AND_REMOVED'
}
