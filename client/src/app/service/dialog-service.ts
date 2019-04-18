import { Injectable } from '@angular/core'
import { MatDialogRef, MatDialog, MatDialogConfig } from '@angular/material'
import { DialogComponent } from "./dialog"
import { Dialog } from "./dialog-module";
import DialogType = Dialog.DialogType;

/**
 * 汎用ダイアログ処理用サービス
 */
@Injectable({
  providedIn: 'root'
})
export class DialogService {
  constructor (
    private dialog: MatDialog
  ) {}

  /**
   * 確認ダイアログを表示する
   */
  public confirm(title: string, content: string): MatDialogRef<DialogComponent> {
    return this.dialog.open(
      DialogComponent,
      this.createDialogConfig(title, content, DialogType.CONFIRM)
    )
  }

  /**
   * エラーダイアログを表示する
   */
  public error(title: string , content: string): MatDialogRef<DialogComponent> {
    return this.dialog.open(
      DialogComponent,
      this.createDialogConfig(title, content, DialogType.ERROR)
    )
  }

  /**
   * INFOダイアログを表示する
   */
  public info(title: string , content: string): MatDialogRef<DialogComponent> {
    return this.dialog.open(
      DialogComponent,
      this.createDialogConfig(title, content, DialogType.INFO)
    )
  }

  /**
   * 警告ダイアログを表示する
   */
  public warning(title: string , content: string): MatDialogRef<DialogComponent> {
    return this.dialog.open(
      DialogComponent,
      this.createDialogConfig(title, content, DialogType.WARNING)
    )
  }

  /**
   * ダイアログ表示用の設定を作成する
   */
  private createDialogConfig (title: string, content: string, type: DialogType): MatDialogConfig {
    return {
      data: {
        title: title,
        content: content,
        type: type
      },
      disableClose: true,
      autoFocus: true
    } as MatDialogConfig
  }
}


