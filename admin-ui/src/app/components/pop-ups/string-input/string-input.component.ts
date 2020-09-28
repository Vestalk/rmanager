import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-string-input',
  templateUrl: './string-input.component.html',
  styleUrls: ['./string-input.component.css']
})
export class StringInputComponent implements OnInit {

  str: string;

  constructor(public dialogRef: MatDialogRef<StringInputComponent>,
              @Inject(MAT_DIALOG_DATA) public data: string) { }

  ngOnInit() {
  }

  closeDialogConfirm() {
    this.dialogRef.close(this.str);
  }

  closeDialogReject() {
    this.dialogRef.close(null);
  }

}
