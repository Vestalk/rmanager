import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {RouterModule, Routes} from "@angular/router";
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatSliderModule} from "@angular/material/slider";
import {MatTableModule} from "@angular/material/table";
import {MatRadioModule} from "@angular/material/radio";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatNativeDateModule} from "@angular/material/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSortModule} from "@angular/material/sort";
import {OverlayModule} from "@angular/cdk/overlay";
import { ContainerComponent } from './components/container/container.component';
import { UsersComponent } from './components/users/users.component';
import { ProductMenuComponent } from './components/product-menu/product-menu.component';
import { StringInputComponent } from './components/pop-ups/string-input/string-input.component';

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {
    path: 'content-container', component: ContainerComponent, children: [
      {path: 'users', component: UsersComponent},
      {path: 'product-menu', component: ProductMenuComponent},
    ]
  },
  {path: '', redirectTo: '/login', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ContainerComponent,
    UsersComponent,
    ProductMenuComponent,
    StringInputComponent
  ],
  imports: [
    MatDialogModule,
    OverlayModule,
    HttpClientModule,
    MatSnackBarModule,
    RouterModule.forRoot(routes, {useHash: true}),
    FormsModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatSliderModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    MatRadioModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  exports: [
      MatSnackBarModule,
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  entryComponents: [
    StringInputComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
