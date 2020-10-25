import { Component, OnInit } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {Order} from "../../model/Order";
import {MatDialog} from "@angular/material/dialog";
import {RequestConfirmComponent} from "../pop-ups/request-confirm/request-confirm.component";

@Component({
  selector: 'app-order-menu',
  templateUrl: './order-menu.component.html',
  styleUrls: ['./order-menu.component.css']
})
export class OrderMenuComponent implements OnInit {

  messageAudio = new Audio();

  CREATED: string = 'CREATED';
  IN_PROGRESS: string = 'IN_PROGRESS';
  REJECTED: string = 'REJECTED';
  ACCEPTED: string = 'ACCEPTED';

  orderList: Order[] = [];
  orderListInProgress: Order[] = [];
  orderListCreated: Order[] = [];

  waitingTimeCookingMap = new Map();
  clientWaitingTimeMap = new Map();

  constructor(private dialog: MatDialog,
              private orderService: OrderService) { }

  ngOnInit() {
    //DOTO fix
    this.messageAudio.src = "https://notificationsounds.com/storage/sounds/file-sounds-1148-juntos.mp3";
    this.messageAudio.load();

    this.loadOrderList();

    setInterval(() => {
      this.orderList.forEach( o => {
        if (o.dateCreate) this.waitingTimeCookingMap.set(o.orderId, this.getWaitingTime(o.dateCreate))
      });
      this.orderList.forEach( o => {
        if (o.dateCooking) this.clientWaitingTimeMap.set(o.orderId, this.getWaitingTime(o.dateCooking))
      });
    }, 1000);

    setInterval(() => {
      let arr = [this.IN_PROGRESS, this.CREATED];
      this.orderService.getOrdersByStatus(arr).subscribe(resp => {
        for (let o of resp) {
          if (!this.checkContainsOrderId(o.orderId)) {
            this.playMessageAudio();
          }
        }
        this.updateOrderLists(resp);
      })
    }, 5000);
  }

  changeOrderStatus(orderId: number, status: string) {
    this.dialog.open(RequestConfirmComponent, {data: null}).afterClosed().subscribe(resp => {
      if (resp) {
        this.orderService.changeStatus(orderId, status).subscribe(resp => {
          //TODO improve performance
          this.loadOrderList();
        })
      }
    });
  }

  loadOrderList() {
    let arr = [this.IN_PROGRESS, this.CREATED];
    this.orderService.getOrdersByStatus(arr).subscribe(resp => {
      this.updateOrderLists(resp);
    })
  }

  updateOrderLists(orderList: Order[]) {
    this.orderList = orderList;
    this.orderListInProgress = orderList.filter(o => o.orderStatus === this.IN_PROGRESS);
    this.orderListCreated = orderList.filter(o => o.orderStatus === this.CREATED);
  }

  getWaitingTime(date: number): string {
    let dateInSecond = (new Date().getTime() - date) / 1000;

    let hour = Math.trunc(dateInSecond / 60 / 60);
    let min = Math.trunc((dateInSecond - (hour * 60 * 60)) / 60);
    let sec = Math.trunc(dateInSecond - (min * 60) - (hour * 60 * 60));

    return (hour < 10 ? '0' + hour : hour) + ':' + (min < 10 ? '0' + min : min) + ':' + (sec < 10 ? '0' + sec : sec);
  }

  playMessageAudio(){
    this.messageAudio.play();
  }

  checkContainsOrderId(orderId: number): boolean {
    for (let o of this.orderList) {
      if (o.orderId === orderId) {
        return true;
      }
    }
    return false;
  }
}
