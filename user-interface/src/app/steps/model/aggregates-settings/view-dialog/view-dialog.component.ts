import { AggregatesSetting } from './../../../../model/model-aggregate';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-view-dialog',
  templateUrl: './view-dialog.component.html',
  styleUrls: ['./view-dialog.component.css']
})
export class ViewDialogComponent implements OnInit {


  constructor(private dialogRef: MatDialogRef<ViewDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public aggregate: AggregatesSetting) {
  }

  ngOnInit(): void {
  }

  close(){
    this.dialogRef.close();
  }

}
