import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {ProductCategoryService} from "../../service/product-category.service";
import {StringInputComponent} from "../pop-ups/string-input/string-input.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-product-menu',
  templateUrl: './product-menu.component.html',
  styleUrls: ['./product-menu.component.css']
})
export class ProductMenuComponent implements OnInit {

  constructor(private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private productCategoryService: ProductCategoryService) { }

  ngOnInit() {
  }

  createNewProductCategory() {
    this.dialog.open(StringInputComponent, {data: 'Название категории'}).afterClosed().subscribe(name => {
      if (name) {
        this.productCategoryService.postNewProductCategory(name).subscribe(resp => {
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
        })
      }
    });
  }

}
