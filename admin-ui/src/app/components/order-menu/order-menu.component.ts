import { Component, OnInit } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {Order} from "../../model/Order";

@Component({
  selector: 'app-order-menu',
  templateUrl: './order-menu.component.html',
  styleUrls: ['./order-menu.component.css']
})
export class OrderMenuComponent implements OnInit {

  orderList: Order[] = [];

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.loadOrderList();
  }

  loadOrderList() {
    this.orderService.getOrdersInProgress().subscribe(resp => this.orderList = resp)
  }

}
