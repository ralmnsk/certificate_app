import {Component, OnInit} from '@angular/core';
import {Order} from './order';
import {OrderService} from '../order/order.service';
import {debounceTime} from 'rxjs/operators';
import {TokenStorageService} from '../auth/token-storage.service';
import {Router} from '@angular/router';
import {DataOrderViewService} from '../data/data-order-view.service';
import {OrderViewStorageService} from '../data/order-view-storage.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  message: string;
  orders: Array<Order>;

  first: number;
  page: number;
  last: number;
  size: number;
  private viewMessage: string;

  constructor(private orderService: OrderService,
              private tokenStorage: TokenStorageService,
              private router: Router,
              private dataOrderViewService: DataOrderViewService,
              private orderViewStorage: OrderViewStorageService
  ) {
    this.first = 0;
    this.size = 5;
  }

  ngOnInit(): void {
    this.first = 0;
    this.getOrders(Number(this.tokenStorage.getId()), this.first, this.size);
    this.dataOrderViewService.currentMessage.subscribe(message => this.viewMessage = message);
  }

  getOrders(userId: number, page?: number, size?: number): void {
    this.orderService.getOrders(userId, page, size)
      .pipe(debounceTime(250))
      .subscribe(
        result => {
          this.last = result.totalPage - 1;
          this.page = result.page;
          const orders = result.elements.content as Array<Order>;
          const loadedOrders = new Array<Order>();
          let count = 0;
          for (const order of orders) {
            this.orderService.getOrder(order.id)
              .subscribe(data => {
                  loadedOrders.push(data as Order);
                  count++;
                  if (count === orders.length) {
                    this.orders = loadedOrders.sort((a, b) => b.id - a.id);
                    this.enableButtons();
                  }
                  // console.log('saved orders:', data as Order);
                }, error => {
                  console.log('error:', error);
                }
              );
          }
        }, error => {
          console.log(error.message);
          this.message = error.error.message;
        }
      );
  }

  toFirstPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.disableButtons();
    this.getOrders(Number(this.tokenStorage.getId()), this.first, this.size);
  }

  toPreviousPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.disableButtons();
    this.page = this.page - 1;
    if (this.page < 0) {
      this.page = 0;
    }
    this.getOrders(Number(this.tokenStorage.getId()), this.page, this.size);
  }

  toNextPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.disableButtons();
    this.page = this.page + 1;
    if (this.page > this.last) {
      this.page = this.last;
    }
    this.getOrders(Number(this.tokenStorage.getId()), this.page, this.size);
  }

  toLastPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.disableButtons();
    this.getOrders(Number(this.tokenStorage.getId()), this.last, this.size);
  }

  view(orderId: number): void {
    this.viewMessage = orderId.toString();
    this.orderViewStorage.setCurrentOrderId(orderId);
    this.dataOrderViewService.changeMessage(this.viewMessage);
    // console.log('view order:', orderId);
    this.router.navigate(['order-view']);
  }

  disableButtons(): void {
    for (let i = 0; i < 4; i++) {
      document.getElementById(i.toString()).className = 'disabled';
    }
  }

  enableButtons(): void {
    for (let i = 0; i < 4; i++) {
      document.getElementById(i.toString()).className = 'nav-btn';
    }
  }

  isButtonsDisabled(): boolean {
    return document.getElementById('0').className === 'disabled';
  }
}
