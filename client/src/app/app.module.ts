import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatCheckboxModule, MatSliderModule,
  MatFormFieldModule, MatInputModule, MatSidenavModule, MatToolbarModule,
  MatListModule, MatDialogModule } from '@angular/material';
import { UiMenuComponent, UiContentComponent, PageMainComponent } from '@app/components';
import { TodoAddModalComponemt } from '@app/components/modal';
import { AppComponent } from '@app/app.component';
import { ErrorNotFoundComponent } from '@app/error';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgxsModule } from '@ngxs/store';
import { TodoState } from '@shared/state';
import { HttpClientModule } from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    UiMenuComponent,
    UiContentComponent,
    PageMainComponent,
    TodoAddModalComponemt,
    ErrorNotFoundComponent
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
    AppRoutingModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxsModule.forRoot([
      TodoState
    ])
  ],
  entryComponents: [
    TodoAddModalComponemt
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
