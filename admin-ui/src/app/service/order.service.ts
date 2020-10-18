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

  getOrdersInProgress(): Observable<Order[]> {
    let url: string = `${this.BASE_URL}/`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.get<Order[]>(url,  {headers});
  }
}
