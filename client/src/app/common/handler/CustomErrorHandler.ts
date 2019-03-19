import {ErrorHandler} from "@angular/core";

/**
 * 動作検証のため、試しに作成したエラーハンドラ
 */
export class CustomErrorHandler implements ErrorHandler {
  handleError(error: any): void {
    console.log('====== error handler =====')
    console.log(error)
  }
}
