import { Injectable } from '@angular/core';
import {User} from "../model/User";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_URL: string = '/user';

  constructor(private http: HttpClient) {
  }

  createUser(user: User): Observable<any> {
    let url: string = `${this.BASE_URL}/createUser`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.post(url, user, {headers});
  }

  getAllUserRole(): Observable<string[]> {
    let url: string = `${this.BASE_URL}/all-roles`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Authentication': `Bearer${token}`
    });
    return this.http.get<string[]>(url, {headers: headers});
  }

  getUserRole(): Observable<any> {
    let url: string = `${this.BASE_URL}/role`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Authentication': `Bearer${token}`
    });
    return this.http.get<any>(url, {headers: headers});
  }

  refreshPassword(login: string, password: string): Observable<any> {
    let url: string = `${this.BASE_URL}/refreshPassword`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    let params = new HttpParams()
      .set('login', login)
      .set('password', password);
    return this.http.post(url, {}, {headers, params});
  }

  updateUserInfo(user: User): Observable<any> {
    let url: string = `${this.BASE_URL}/updateUserInfo`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.post(url, user, {headers});
  }

  getUserInfo(): Observable<User> {
    let url: string = `${this.BASE_URL}/userInfo`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.get<User>(url, {headers});
  }

  getUsers(): Observable<User[]> {
    let url: string = `${this.BASE_URL}/userList`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.get<User[]>(url, {headers: headers});
  }

  deleteUser(userId: string): Observable<any> {
    let url: string = `${this.BASE_URL}/delete`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    let params = new HttpParams().set('userId', userId);
    return this.http.delete(url, {headers, params});
  }
}
