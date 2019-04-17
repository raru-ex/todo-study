import { BrowserModule } from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule, MatCheckboxModule, MatSliderModule,
  MatFormFieldModule, MatInputModule, MatSidenavModule, MatToolbarModule,
  MatListModule, MatDialogModule, MatSnackBarModule, MatSelectModule, MatOptionModule
} from '@angular/material';
import { UiMenuComponent, UiContentComponent, PageMainComponent } from '@app/components';
import { TodoAddModalComponent, TodoEditModalComponent, TodoDeleteModalComponent } from '@app/components/modal';
import { AppComponent } from '@app/app.component';
import { ErrorNotFoundComponent } from '@app/error';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgxsModule } from '@ngxs/store';
import { TodoState } from '@app/state';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { MatIconModule } from '@angular/material/icon'
import { CustomHttpErrorInterceptor } from "@app/common/http/CustomHttpErrorInterceptor";
import { CustomErrorHandler } from '@app/common/handler/CustomErrorHandler'
import {DialogComponent} from "@app/service/dialog";

@NgModule({
  declarations: [
    AppComponent,
    UiMenuComponent,
    UiContentComponent,
    PageMainComponent,
    TodoAddModalComponent,
    TodoEditModalComponent,
    TodoDeleteModalComponent,
    ErrorNotFoundComponent,
    DialogComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatSliderModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatSidenavModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    MatDialogModule,
    MatSnackBarModule,
    MatIconModule,
    MatSelectModule,
    MatOptionModule,
    AppRoutingModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxsModule.forRoot([
      TodoState
    ])
  ],
  entryComponents: [
    TodoAddModalComponent,
    TodoEditModalComponent,
    TodoDeleteModalComponent,
    DialogComponent,
  ],
  providers: [{
    provide: ErrorHandler,
    useClass: CustomErrorHandler
  },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomHttpErrorInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
