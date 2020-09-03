import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavigationComponent} from './navigation/navigation.component';
import {MainComponent} from './main/main.component';
import {FooterComponent} from './footer/footer.component';
import {ContainerComponent} from './container/container.component';
import {HeaderComponent} from './header/header.component';
import {LoginComponent} from './login/login.component';
import {HttpClientModule} from '@angular/common/http';
import {TokenStorageService} from './auth/token-storage.service';
import {AuthService} from './auth/auth.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserService} from './user/user.service';
import {UserComponent} from './user/user.component';
import {RegisterComponent} from './register/register.component';
import {RegisterService} from './register/register.service';
import {DataService} from './data/data.service';
import {AuthInterceptor} from './auth/auth.interceptor';
import {UsersComponent} from './users/users.component';
import {OrdersComponent} from './orders/orders.component';
import {InfoComponent} from './user/info/info.component';
import {CertificatesComponent} from './certificates/certificates.component';
import {DataTokenService} from './data/data-token.service';
import {CertificatesService} from './certificates/certificates.service';
import {DataCertificateService} from './data/data-certificate.service';
import {CertificateStorageService} from './data/certificate-storage.service';
import { TagsComponent } from './tags/tags.component';
import {TagsService} from './tags/tags.service';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    MainComponent,
    NavigationComponent,
    ContainerComponent,
    HeaderComponent,
    LoginComponent,
    UserComponent,
    RegisterComponent,
    UsersComponent,
    OrdersComponent,
    InfoComponent,
    CertificatesComponent,
    TagsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    TokenStorageService,
    AuthService,
    UserService,
    RegisterService,
    DataService,
    AuthInterceptor,
    DataTokenService,
    DataCertificateService,
    CertificatesService,
    CertificateStorageService,
    TagsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
