import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';

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
import {RegisterService} from './register/register.service';
import {DataService} from './data/data.service';
import {UsersComponent} from './users/users.component';
import {OrdersComponent} from './orders/orders.component';
import {InfoComponent} from './user/info/info.component';
import {CertificatesComponent} from './certificates/certificates.component';
import {DataTokenService} from './data/data-token.service';
import {CertificatesService} from './certificates/certificates.service';
import {DataCertificateService} from './data/data-certificate.service';
import {CertificateStorageService} from './data/certificate-storage.service';
import {TagsComponent} from './tags/tags.component';
import {TagsService} from './tags/tags.service';
import {DataTagService} from './data/data-tag.service';
import {TagStorageService} from './data/tag-storage.service';
import {CertificateComponent} from './certificate/certificate.component';
import {DataTagEditService} from './data/data-tag-edit.service';
import {CertificateService} from './certificate/certificate.service';
import {CertificateDeletedComponent} from './certificate-deleted/certificate-deleted.component';
import {CreateCertificateComponent} from './create-certificate/create-certificate.component';
import {OrderComponent} from './order/order.component';
import {OrderService} from './order/order.service';
import {OrderStorageService} from './data/order-storage.service';
import {OrderViewComponent} from './order-view/order-view.component';
import {DataOrderViewService} from './data/data-order-view.service';
import {OrderViewStorageService} from './data/order-view-storage.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {OrderAdminViewComponent} from './order-admin-view/order-admin-view.component';
import {UserAdminViewStorageService} from './data/user-admin-view-storage.service';
import {MatMenuModule} from '@angular/material/menu';
import {MatIconModule} from '@angular/material/icon';
import {MatTableModule} from '@angular/material/table';
import {AuthGuard} from './auth/auth.guard';
import {ErrorInterceptor} from './auth/error.interceptor';
import {JwtInterceptor} from './auth/jwt.interceptor';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatInputModule} from '@angular/material/input';
import {RegisterComponent} from './register/register.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';

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
    OrderAdminViewComponent,
    InfoComponent,
    CertificatesComponent,
    TagsComponent,
    CertificateComponent,
    CertificateDeletedComponent,
    CreateCertificateComponent,
    OrderComponent,
    OrderViewComponent,
    OrderAdminViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatMenuModule,
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatInputModule,
    MatAutocompleteModule
  ],
  providers: [
    AuthGuard,
    AuthService,
    CertificateService,
    CertificatesService,
    CertificateStorageService,
    DataService,
    DataTokenService,
    DataCertificateService,
    DataOrderViewService,
    DataTagService,
    DataTagEditService,
    ErrorInterceptor,
    JwtInterceptor,
    OrderViewStorageService,
    OrderService,
    OrderStorageService,
    RegisterService,
    TagsService,
    TagStorageService,
    TokenStorageService,
    UserService,
    UserAdminViewStorageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
