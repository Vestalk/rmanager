import {Component, Inject, OnInit} from '@angular/core';
import {Img} from "../../../model/Img";
import {Product} from "../../../model/Product";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProductCategoryService} from "../../../service/product-category.service";
import {ProductCategory} from "../../../model/ProductCategory";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-create-edit-product',
  templateUrl: './create-edit-product.component.html',
  styleUrls: ['./create-edit-product.component.css']
})
export class CreateEditProductComponent implements OnInit {

  url;
  uploadButtonName: string = 'Выбрать картинку';
  product: Product = null;

  productCategoryList: ProductCategory[] = [];

  constructor(public dialogRef: MatDialogRef<CreateEditProductComponent>,
              private productCategoryService: ProductCategoryService,
              private snackBar: MatSnackBar,
              @Inject(MAT_DIALOG_DATA) public data: Product) {
  }

  ngOnInit() {
    this.product = JSON.parse(JSON.stringify(this.data))
    if (this.product.img) {
      document.getElementById("product_img").innerHTML = "<img _ngcontent-eqr-c3 alt=\"image_error\" src=\"" + this.product.img.imageContent + "\">";
      this.uploadButtonName = 'Изменить картинку';
    }
    this.productCategoryService.getAllProductCategory().subscribe(resp => {
      this.productCategoryList = resp;
    })
  }

  onFileSelected(event) {
    let image = new Image();
    image.onload = () => {
      if (document.getElementById("product_img").childNodes.length !== 0) {
        document.getElementById("product_img").innerHTML = '';
      }
      document.getElementById("product_img").append(image);

      let img = new Img();
      img.imageContent =  event.target.files[0];
      this.product.img = img;

      this.uploadButtonName = 'Изменить картинку';
    };
    image.src = URL.createObjectURL(event.target.files[0]);
  }

  closeDialogConfirm() {
    if (this.product.name && this.product.name.trim() !== '' &&
      this.product.description && this.product.description.trim() !== '' &&
      this.product.img && this.product.cost) {
      this.dialogRef.close(this.product);
    } else {
      this.snackBar.open('Заполните поля!', 'OK', {
        duration: 2000,
      });
    }
  }

  closeDialogReject() {
    this.dialogRef.close();
  }

}
