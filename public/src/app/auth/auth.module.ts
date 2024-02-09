import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { LoginComponent, SignupComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';



@NgModule({
  declarations: [
    LoginComponent,
    SignupComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [
    provideHttpClient(withFetch())
  ]
})
export class AuthModule { }
