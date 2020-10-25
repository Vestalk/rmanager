import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Order} from "../model/Order";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private BASE_URL: string = '/order';

  constructor(private http: HttpClient) {
  }

  getOrdersByStatus(statusArr: string[]): Observable<Order[]> {
    let url: string = `${this.BASE_URL}/`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });

    let params = new HttpParams();
    params = params.set('orderStatusList', JSON.stringify(statusArr));

    return this.http.get<Order[]>(url,  {headers, params});
  }

  changeStatus(orderId: number, orderStatus: string): Observable<Order> {
    let url: string = `${this.BASE_URL}/change_status/${orderId}/${orderStatus}`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });

    return this.http.get<Order>(url,  {headers});
  }

  gg(date: number) {
    return ;
  }
}
