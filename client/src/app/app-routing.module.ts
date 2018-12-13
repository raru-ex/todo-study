import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageMainComponent } from '@app/components';
import { ErrorNotFoundComponent } from '@app/error/error-not-found';

const routes: Routes = [
  { path: '', component: PageMainComponent },
  { path: '**', component: ErrorNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
