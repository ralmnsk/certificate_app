import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {UserComponent} from './user/user.component';
import {RegisterComponent} from './register/register.component';
import {UsersComponent} from './users/users.component';
import {OrdersComponent} from './orders/orders.component';
import {CertificatesComponent} from './certificates/certificates.component';


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'user', component: UserComponent},
  {path: 'users', component: UsersComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'certificates', component: CertificatesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
