import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Token} from "../model/Token";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private BASE_URL: string = '/auth';

  constructor(private http : HttpClient) {}

  login(user): Observable<Token>{
    let url: string = `${this.BASE_URL}/login`;
    return this.http.post<Token>(url, user);
  }

  logout(): Observable<any> {
    let url: string = `${this.BASE_URL}/logout`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.delete(url, {headers: headers});
  }

  status(): Observable<any>{
    let url: string = `${this.BASE_URL}/status`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.get(url, {headers: headers});
  }

}
