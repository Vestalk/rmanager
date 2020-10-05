import {Component, Inject, OnInit} from '@angular/core';
import {Img} from "../../../model/Img";
import {Product} from "../../../model/Product";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-create-edit-product',
  templateUrl: './create-edit-product.component.html',
  styleUrls: ['./create-edit-product.component.css']
})
export class CreateEditProductComponent implements OnInit {

  url;
  uploadButtonName: string = 'Выбрать картинку';
  product: Product = null;

  constructor(public dialogRef: MatDialogRef<CreateEditProductComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Product) { }

  ngOnInit() {
    this.product = JSON.parse(JSON.stringify(this.data))
  }

  onFileSelected235(event) {
    let image = new Image();
    image.onload = () => {
      if (document.getElementById("offer_img").childNodes.length !== 0) {
        document.getElementById("offer_img").innerHTML = '';
      }
      document.getElementById("offer_img").append(image);

      let img = new Img();
      img.imageContent =  event.target.files[0];
      this.product.img = img;

      this.uploadButtonName = 'Изменить картинку';
    };
    image.src = URL.createObjectURL(event.target.files[0]);
  }

  closeDialogConfirm() {
    this.dialogRef.close(this.product);
  }

  closeDialogReject() {
    this.dialogRef.close();
  }

}
