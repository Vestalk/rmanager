import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
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

    if (product.categoryId) {
      fd.append("categoryId", product.categoryId.toString());
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
    if (!product.img.imgId) {
      if (product.img.imageContent != null) {
        fd.append('image', product.img.imageContent, product.img.imageContent.name);
      }
    }

    fd.append("productId", product.productId.toString());
    fd.append("name", product.name);
    fd.append("description", product.description);
    fd.append("cost", product.cost.toString());
    fd.append("isAvailable", product.isAvailable.toString());

    if (product.categoryId) {
      fd.append("categoryId", product.categoryId.toString());
    }

    return this.http.post<Product>(url, fd, {headers: headers});
  }

  getProductPage(isAvailable: boolean, productCategoryId: number, page: number, pageSize: number): Observable<Product[]> {
    let url: string = `${this.BASE_URL}`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    let params = new HttpParams()
      .set('page', page.toString())
      .set('pageSize', pageSize.toString());
    if (isAvailable !== null) {
      params = params.set("isAvailable", isAvailable.toString());
    }
    if (productCategoryId !== null) {
      params = params.set("productCategoryId", productCategoryId.toString());
    }
    return this.http.get<Product[]>(url,  {headers, params});
  }

  countProduct(isAvailable: boolean, productCategoryId: number): Observable<number> {
    let url: string = `${this.BASE_URL}/count`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });
    let params = new HttpParams();
    if (isAvailable !== null) {
      params = params.set("isAvailable", isAvailable.toString());
    }
    if (productCategoryId !== null) {
      params = params.set("productCategoryId", productCategoryId.toString());
    }
    return this.http.get<number>(url,  {headers, params});
  }

  archiveProduct(productId: number): Observable<any> {
    let url: string = `${this.BASE_URL}/${productId}`;
    let token = localStorage.getItem('tokenAdvMgmgt');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authentication': `Bearer${token}`
    });

    return this.http.delete<any>(url,  {headers});
  }

}
