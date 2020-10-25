import {TelegramUser} from "./TelegramUser";
import {OrderItem} from "./OrderItem";

export class Order {

  orderId: number;
  orderStatus: string;
  dateCreate: number;
  dateExecute: number;
  dateCooking: number;
  paymentMethod: string;

  client: TelegramUser;
  orderItemList: OrderItem[] = [];

}
