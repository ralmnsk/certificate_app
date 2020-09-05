import {Component, OnInit} from '@angular/core';
import {Certificate} from '../certificates/certificate';
import {Router} from '@angular/router';
import {Order} from '../orders/order';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  certificates: Array<Certificate>;
  order: Order;
  message: string;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  cancel(): void {
    this.order = new Order();
    this.certificates = new Array<Certificate>();
  }

  save() {

  }
}
