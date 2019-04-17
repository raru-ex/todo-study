import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { DialogServiceData, DialogType } from "./dialog-service";


@Component({
  templateUrl: './dialog.html',
  styleUrls: ['./dialog.scss']
})
export class DialogComponent {
  dialogType = DialogType

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DialogServiceData,
    public dialogRef: MatDialogRef<DialogComponent>
  ) { }

  ngOnInit(): void {
    console.log('dialog service is called')
  }
}