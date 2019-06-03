import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {DialogService} from '@app/service/dialog-service';
import {Injectable} from '@angular/core';


/** Httpエラーのハンドリングを行うインターセプタ */
@Injectable()
export class CustomHttpErrorInterceptor implements HttpInterceptor {
  constructor(
    private dialog: DialogService
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
            this.showError(`code: ${error.status}, message: ${error.message}`);
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
  private showError(message): void {
    this.dialog.error('エラー', message);
  }
}

