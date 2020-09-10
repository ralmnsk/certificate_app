import {Component, OnInit} from '@angular/core';
import {Order} from '../orders/order';
import {TokenStorageService} from '../auth/token-storage.service';
import {Router} from '@angular/router';
import {DataService} from '../data/data.service';
import {OrderService} from '../order/order.service';
import {OrderViewStorageService} from '../data/order-view-storage.service';
import {DataOrderViewService} from '../data/data-order-view.service';
import {UserAdminViewStorageService} from '../data/user-admin-view-storage.service';
import {UserService} from '../user/user.service';

@Component({
  selector: 'app-order-admin-view',
  templateUrl: './order-admin-view.component.html',
  styleUrls: ['./order-admin-view.component.css']
})
export class OrderAdminViewComponent implements OnInit {
  // userId: number;
  userSurnameView: string;
  userNameView: string;
  orders: Array<Order>;
  firstOrderPage = 0;
  currentOrderPage: number;
  lastOrderPage: number;
  orderPageSize: number;
  private viewMessage: string;
  private message: string;
  userViewId: number;
  displayedColumns: string[] = ['View', 'Total cost', 'Created', 'Description', 'Completed'];
  isProcessBar: boolean;


  constructor(private tokenStorage: TokenStorageService,
              private router: Router,
              private dataService: DataService,
              private orderService: OrderService,
              private orderViewStorage: OrderViewStorageService,
              private dataOrderViewService: DataOrderViewService,
              private userAdminViewStorageService: UserAdminViewStorageService,
              private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.isProcessBar = true;
    this.initOrderPagination();
    this.initUser();
    this.userAdminViewStorageService.getChangedUserView.subscribe(
      data => {
        this.initUser();
      }
    );
  }

  initUser(): void {
    this.userViewId = this.userAdminViewStorageService.getCurrentUserId();
    this.userSurnameView = this.userAdminViewStorageService.getSurname();
    this.userNameView = this.userAdminViewStorageService.getName();
    console.log('order-admin-view:', this.userViewId, this.userSurnameView, this.userNameView);
    this.viewOrders(this.userViewId, this.userSurnameView, this.userNameView, 0);
  }

  initOrderPagination(): void {
    this.firstOrderPage = 0;
    this.currentOrderPage = 0;
    this.orderPageSize = 10;
    this.lastOrderPage = 0;
  }

  viewOrders(id: number, surname: string, name: string, page: number): void {
    this.isProcessBar = true;
    if (this.userViewId !== null && this.userViewId !== undefined) {
      this.disableOrderNavigationButtons();
    }
    this.userViewId = id;
    this.userSurnameView = surname;
    this.userNameView = name;
    this.orderService.getOrders(id, page, this.orderPageSize)
      .subscribe(
        data => {
          this.orders = data.elements.content as Array<Order>;
          this.lastOrderPage = data.totalPage - 1;
          console.log('user orders:', this.orders);
          this.enableOrderNavigationButtons();
          this.isProcessBar = false;
        },
        error => {
          this.message = error.error.message;
        }
      );
  }

  toFirstOrderPage(): void {
    if (this.isOrderButtonsDisabled()) {
      return;
    }
    this.viewOrders(this.userViewId, this.userSurnameView, this.userNameView, 0);
  }

  toPreviousOrderPage(): void {
    if (this.isOrderButtonsDisabled()) {
      return;
    }
    this.currentOrderPage = this.currentOrderPage.valueOf() - 1;
    if (this.currentOrderPage < 0) {
      this.currentOrderPage = 0;
    }
    this.viewOrders(this.userViewId, this.userSurnameView, this.userNameView, this.currentOrderPage);
  }

  toNextOrderPage(): void {
    if (this.isOrderButtonsDisabled()) {
      return;
    }
    this.currentOrderPage = this.currentOrderPage.valueOf() + 1;
    if (this.currentOrderPage > this.lastOrderPage) {
      this.currentOrderPage = this.lastOrderPage;
    }
    this.viewOrders(this.userViewId, this.userSurnameView, this.userNameView, this.currentOrderPage);
  }

  toLastOrderPage(): void {
    if (this.isOrderButtonsDisabled()) {
      return;
    }
    this.currentOrderPage = this.lastOrderPage;
    this.viewOrders(this.userViewId, this.userSurnameView, this.userNameView, this.currentOrderPage);
  }

  disableOrderNavigationButtons(): void {
    for (let i = 4; i < 8; i++) {
      if (document.getElementById(i.toString()) !== null && document.getElementById(i.toString()) !== undefined) {
        document.getElementById(i.toString()).className = 'disabled';
      }
    }
    console.log('disabled navigation buttons');
  }

  enableOrderNavigationButtons(): void {
    for (let i = 4; i < 8; i++) {
      document.getElementById(i.toString()).className = 'nav-btn';
    }
    console.log('enable navigation buttons');
  }

  isOrderButtonsDisabled(): boolean {
    if (document.getElementsByClassName('disabled').length > 0) {
      return true;
    }
    return false;
  }

  view(orderId: number): void {
    this.viewMessage = orderId.toString();
    this.orderViewStorage.setCurrentOrderId(orderId);
    this.dataOrderViewService.changeMessage(this.viewMessage);
    this.router.navigate(['order-view']);
  }
}
