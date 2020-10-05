import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Product} from "../model/Product";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private BASE_URL: string = '/product';

  constructor(private http: HttpClient) {
  }

  createNewProduct(product: Product): Observable<Product> {
    let url: string = `${this.BASE_URL}/`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Authentication': `Bearer${token}`
    });

    const fd = new FormData();
    if (product.img) {
      if (product.img.imageContent != null) {
        fd.append('image', product.img.imageContent, product.img.imageContent.name);
      }
    }

    fd.append("name", product.name);
    fd.append("description", product.description);
    fd.append("cost", product.cost.toString());
    fd.append("isAvailable", product.isAvailable.toString());

    if (product.category) {
      fd.append("category", JSON.stringify(product.category));
    }

    return this.http.post<Product>(url, fd, {headers: headers});
  }

  editProduct(product: Product): Observable<Product> {
    let url: string = `${this.BASE_URL}/edit`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Authentication': `Bearer${token}`
    });

    const fd = new FormData();
    if (product.img) {
      if (product.img.imageContent != null) {
        fd.append('image', product.img.imageContent, product.img.imageContent.name);
      }
    }

    fd.append("name", product.name);
    fd.append("description", product.description);
    fd.append("cost", product.cost.toString());
    fd.append("isAvailable", product.isAvailable.toString());

    if (product.category) {
      fd.append("category", JSON.stringify(product.category));
    }

    return this.http.post<Product>(url, fd, {headers: headers});
  }

}
