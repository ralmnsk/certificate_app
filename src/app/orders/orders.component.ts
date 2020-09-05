import {Component, OnInit} from '@angular/core';
import {Order} from './order';
import {OrderService} from '../order/order.service';
import {debounceTime} from 'rxjs/operators';
import {TokenStorageService} from '../auth/token-storage.service';

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

  constructor(private orderService: OrderService,
              private tokenStorage: TokenStorageService
  ) {
  }

  ngOnInit(): void {
    this.getOrders(Number(this.tokenStorage.getId()), 0, 20);
  }

  getOrders(userId: number, page?: number, size?: number): void {
    this.orderService.getOrders(userId, page, size)
      .pipe(debounceTime(1000))
      .subscribe(
        result => {
          this.orders = result.elements.content as Array<Order>;
          // this.message = 'Orders were got.';
        }, error => {
          console.log(error.message);
          this.message = 'Error happened during orders getting.';
        }
      );
  }

  toFirstPage(): void {

  }

  toPreviousPage(): void {

  }

  toNextPage(): void {

  }

  toLastPage(): void {

  }
}
