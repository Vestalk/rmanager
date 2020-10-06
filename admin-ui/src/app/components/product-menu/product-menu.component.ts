import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {ProductCategoryService} from "../../service/product-category.service";
import {StringInputComponent} from "../pop-ups/string-input/string-input.component";
import {MatDialog} from "@angular/material/dialog";
import {ProductCategory} from "../../model/ProductCategory";
import {CreateEditProductComponent} from "../pop-ups/create-edit-product/create-edit-product.component";
import {Product} from "../../model/Product";
import {ProductService} from "../../service/product.service";

@Component({
  selector: 'app-product-menu',
  templateUrl: './product-menu.component.html',
  styleUrls: ['./product-menu.component.css']
})
export class ProductMenuComponent implements OnInit {

  selectCategoryId: number = null;
  productCategoryList: ProductCategory[] = [];

  productList: Product[] = [];
  length: number =  10

  constructor(private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private productService: ProductService,
              private productCategoryService: ProductCategoryService) { }

  ngOnInit() {
    this.uploadProductCategoryList();
    this.uploadProductList(null, null, 0, this.length);
  }

  createNewProductCategory() {
    this.dialog.open(StringInputComponent, {data: 'Название категории'}).afterClosed().subscribe(name => {
      if (name) {
        this.productCategoryService.postNewProductCategory(name).subscribe(resp => {
          this.uploadProductCategoryList();
          this.snackBar.open('Добавлено!', 'OK', {
            duration: 2000,
          });
        }, err => {
          if ( err.status === 400) {
            this.snackBar.open('Категория с таким названием уже существует!', 'OK', {
              duration: 2000,
            });
          } else {
            this.snackBar.open('Что то пошло не так!', 'OK', {
              duration: 2000,
            });
          }
          this.uploadProductCategoryList();
        })
      }
    });
  }

  createNewProduct() {
    let product = new Product();
    product.isAvailable = true;
    this.dialog.open(CreateEditProductComponent, {data: product}).afterClosed().subscribe(resp => {
      this.productService.createNewProduct(resp).subscribe(resp => {
        this.snackBar.open('Добавлено!', 'OK', {
          duration: 2000,
        });
        this.uploadProductList(null, this.selectCategoryId, 0, this.length);
      }, err => {
        this.snackBar.open('Что то пошло не так!', 'OK', {
          duration: 2000,
        });
      })
    });
  }

  editProduct(item: Product) {
    this.dialog.open(CreateEditProductComponent, {data: item}).afterClosed().subscribe(resp => {
      this.productService.editProduct(resp).subscribe(resp => {
        this.snackBar.open('Сохранено!', 'OK', {
          duration: 2000,
        });
        this.uploadProductList(null, this.selectCategoryId, 0, this.length);
      }, err => {
        this.snackBar.open('Что то пошло не так!', 'OK', {
          duration: 2000,
        });
      })
    });
  }

  deleteProduct(item: Product) {
    this.productService.archiveProduct(item.productId).subscribe(resp => {
      this.snackBar.open('Удалено!', 'OK', {
        duration: 2000,
      });
      this.uploadProductList(null, this.selectCategoryId, 0, this.length);
    })
  }

  uploadProductCategoryList() {
    this.productCategoryService.getAllProductCategory().subscribe(resp => this.productCategoryList = resp);
  }

  uploadProductList(isAvailable: boolean, productCategoryId: number, page: number, pageSize: number) {
    this.productService.countProduct(isAvailable, productCategoryId).subscribe(resp => this.length = resp);
    this.productService.getProductPage(isAvailable, productCategoryId, page, pageSize).subscribe(resp => this.productList = resp);
  }

  changeSelectCategory(categoryId: number) {
    if (this.selectCategoryId === categoryId) {
      this.selectCategoryId = null;
    } else {
      this.selectCategoryId = categoryId;
    }
    this.uploadProductList(null, this.selectCategoryId, 0, this.length);
  }

}
