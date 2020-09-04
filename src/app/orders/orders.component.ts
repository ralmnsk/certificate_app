import {Component, OnInit} from '@angular/core';
import {Order} from './order';

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

  constructor() {
  }

  ngOnInit(): void {
  }

  toFirstPage() {

  }

  toPreviousPage() {

  }

  toNextPage() {

  }

  toLastPage() {

  }
}
