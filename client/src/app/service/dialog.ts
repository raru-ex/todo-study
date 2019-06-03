import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Dialog } from './dialog-module';
import DialogType        = Dialog.DialogType;
import DialogServiceData = Dialog.DialogServiceData;


@Component({
  templateUrl: './dialog.html',
  styleUrls: ['./dialog.scss']
})
export class DialogComponent {
  dialogType = DialogType;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DialogServiceData,
    public dialogRef: MatDialogRef<DialogComponent>
  ) { }
}

