import {ValidatorFn, ValidationErrors, FormGroup} from "@angular/forms"

export class CustomValidators {

  /**
   * 条件がtrueのときにvalidatorを適用する
   *
   * TODO: [注意]
   * 本validator以外のvalidatorが同一フィールドに適用されていると
   * そっちのエラーごと消えるので正常にうごかない。
   * ただどうしようもない
   * @param validatedKey validator対象のFormControlName
   * @param conditionKey conditionの条件になるFormControlName
   * @param condition conditionKeyの値を引数にとる、validate条件計算クロージャ
   * @param validators 適用したいvalidator
   */
  static validateIf(validatedKey: string, conditionKey: string, condition: (value) => boolean, validators: ValidatorFn | ValidatorFn[]): ValidatorFn {
    return (group: FormGroup): ValidationErrors => {
      // validate対象と条件対象の項目を取得
      const validatedControl = group.get(validatedKey)
      const conditionControl = group.get(conditionKey)

      // 入力が複数化、一つかを判別
      if (Array.isArray(validators)) {

        if (condition(conditionControl.value)) {
          let mergedErrors = {}
          // 各validatorを実行して結果をマージ
          validators.forEach(validator => {
            Object.assign(mergedErrors, validator(validatedControl))
          })

          // エラーがある場合にはvalidate対象のコントロールにエラーをセット
          if (Object.keys(mergedErrors).length !== 0) {
            validatedControl.setErrors(mergedErrors)
            return mergedErrors
          }
        }

        // 入力チェックの必要がない場合とエラーがない場合にエラーを削除
        validatedControl.setErrors(null)
        return null
      } else {
        if (condition(conditionControl.value)) {
          const error = validators(validatedControl)
          if (Object.keys(error).length !== 0) {
            validatedControl.setErrors(error)
            return error
          }
        }
        validatedControl.setErrors(null)
        return
      }
    }
  }
}