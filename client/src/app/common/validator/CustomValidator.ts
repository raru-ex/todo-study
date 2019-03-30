import { ValidatorFn, AbstractControl, ValidationErrors} from "@angular/forms"

export class CustomValidators {

   static validateIf(condition: () => boolean, validator: ValidatorFn): ValidatorFn {
     return (control: AbstractControl): ValidationErrors => {
       return condition() ? validator(control) : null
     }
   }
}