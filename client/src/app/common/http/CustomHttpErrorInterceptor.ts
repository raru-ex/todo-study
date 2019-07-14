import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {DialogService} from '@app/service/dialog-service';
import {Injectable} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {DialogComponent} from '@app/service/dialog';


/** Httpエラーのハンドリングを行うインターセプタ */
@Injectable()
export class CustomHttpErrorInterceptor implements HttpInterceptor {
  constructor(
    private dialog: DialogService,
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
    .pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        console.log(error);
        // クライアントエラー
        // TODO: たぶんこのクライアントエラーで分岐できてない
        if (error.error instanceof ErrorEvent) {
          this.showError(error.error.message);
        } else { // サーバエラー
          this.handleServerError(error)
        }
        // throwErrorの引数が、その後のErrorHandlerのerror引数まで渡っていく
        return throwError('this is test');
      })
    );
  }

  /**
   * エラーメッセージを表示する。
   * 一旦アラートで表示
   */
  private showError(message: string): MatDialogRef<DialogComponent> {
    return this.dialog.error('エラー', message);
  }

  /**
   * サーバエラーの処理を行う
   */
  private handleServerError(error: HttpErrorResponse) {
    if (error.status === 401) {
      this.showError(`セッションの有効期限が切れています。`).afterClosed().subscribe(_ => {
        // 今回はloginはangular側で管理していないためlocationで飛ばす
        window.location.href = "/login"
      });
    } else {
      this.showError(`code: ${error.status}, message: ${error.message}`);
    }
  }
}

