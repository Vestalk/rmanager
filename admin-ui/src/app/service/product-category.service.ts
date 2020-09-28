import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProductCategory} from "../model/ProductCategory";

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {

  private BASE_URL: string = '/product-category';

  constructor(private http: HttpClient) {
  }

  postNewProductCategory(categoryName: string): Observable<ProductCategory> {
    let url: string = `${this.BASE_URL}/`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.post<ProductCategory>(url, categoryName, {headers});
  }

  getAllProductCategory(): Observable<ProductCategory[]> {
    let url: string = `${this.BASE_URL}/`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    return this.http.get<ProductCategory[]>(url,  {headers});
  }
}
