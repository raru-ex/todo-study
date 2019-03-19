import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, retry} from "rxjs/operators";


/** Httpエラーのハンドリングを行うインターセプタ */
export class CustomHttpErrorInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        retry(1),
        catchError((error: HttpErrorResponse) => {
          // クライアントエラー
          if (error.error instanceof ErrorEvent) {
            this.showError(error.error.message)
          } else { // サーバエラー
            this.showError(`code: ${error.status}, message: ${error.message}`)
          }
          // throwErrorの引数が、その後のErrorHandlerのerror引数まで渡っていく
          return throwError("this is test")
          })
      )
  }

  /**
   * エラーメッセージを表示する。
   * 一旦アラートで表示
   */
  private showError(message): void {
    window.alert(message)
  }
}