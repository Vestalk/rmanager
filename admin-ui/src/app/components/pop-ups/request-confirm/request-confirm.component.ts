import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-request-confirm',
  templateUrl: './request-confirm.component.html',
  styleUrls: ['./request-confirm.component.css']
})
export class RequestConfirmComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<RequestConfirmComponent>,
              @Inject(MAT_DIALOG_DATA) public data: string) { }

  ngOnInit() {
  }

  accept() {
    this.dialogRef.close(true);
  }

  reject() {
    this.dialogRef.close(false);
  }

}
