import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {UserComponent} from './user/user.component';
import {RegisterComponent} from './register/register.component';
import {UsersComponent} from './users/users.component';
import {OrdersComponent} from './orders/orders.component';
import {CertificatesComponent} from './certificates/certificates.component';
import {CertificateComponent} from './certificate/certificate.component';
import {CertificateDeletedComponent} from './certificate-deleted/certificate-deleted.component';
import {CreateCertificateComponent} from './create-certificate/create-certificate.component';
import {OrderComponent} from './order/order.component';
import {OrderViewComponent} from './order-view/order-view.component';
import {OrderAdminViewComponent} from './order-admin-view/order-admin-view.component';
import {AuthGuard} from './auth/auth.guard';


const routes: Routes = [
  {path: '', component: CertificatesComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'user', component: UserComponent, canActivate: [AuthGuard]},
  {path: 'users', component: UsersComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN']}},
  {path: 'orders', component: OrdersComponent, canActivate: [AuthGuard]},
  {path: 'order', component: OrderComponent, canActivate: [AuthGuard]},
  {path: 'order-view', component: OrderViewComponent, canActivate: [AuthGuard]},
  {path: 'order-admin-view', component: OrderAdminViewComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN']}},
  {path: 'certificates', component: CertificatesComponent},
  {path: 'certificate', component: CertificateComponent},
  {path: 'certificate-deleted', component: CertificateDeletedComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN']}},
  {path: 'create-certificate', component: CreateCertificateComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN']}},
  {path: '**', redirectTo: 'certificates'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
