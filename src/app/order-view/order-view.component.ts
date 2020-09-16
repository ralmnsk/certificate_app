import {Component, OnInit} from '@angular/core';
import {DataOrderViewService} from '../data/data-order-view.service';
import {Order} from '../orders/order';
import {OrderService} from '../order/order.service';
import {Certificate} from '../certificates/certificate';
import {FormControl} from '@angular/forms';
import {Router} from '@angular/router';
import {CertificateService} from '../certificate/certificate.service';
import {OrderViewStorageService} from '../data/order-view-storage.service';
import {CertificateStorageService} from '../data/certificate-storage.service';
import {TokenStorageService} from '../auth/token-storage.service';
import {DataModalService} from '../data/data-modal.service';
import {FALSE, ORDER_VIEW} from '../modal/modal.component';

@Component({
  selector: 'app-order-view',
  templateUrl: './order-view.component.html',
  styleUrls: ['./order-view.component.css']
})
export class OrderViewComponent implements OnInit {
  orderId: number;
  order: Order;
  message: string;
  certificates: Array<Certificate>;
  totalCost: number;
  description = new FormControl();

  first: number;
  page: number;
  last: number;
  size: number;

  isProcessBar: boolean;
  displayedColumns: string[] = ['View', 'Id', 'Name', 'Price', 'Creation', 'Modification', 'Description'];

  constructor(private dataOrderViewService: DataOrderViewService,
              private orderService: OrderService,
              private orderViewStorage: OrderViewStorageService,
              private certificateService: CertificateService,
              private router: Router,
              private certificateStorage: CertificateStorageService,
              private tokenStorage: TokenStorageService,
              private dataModal: DataModalService
  ) {
  }

  ngOnInit(): void {
    this.dataModal.changeMessage(FALSE);
    this.isProcessBar = true;
    this.first = 0;
    this.page = 0;
    this.size = 50;
    this.orderId = this.orderViewStorage.getCurrentOrderId();
    this.getOrder(this.orderId);
    console.log('order view ng init');
    this.dataModal.backMessage.subscribe(
      data => {
        if (data === 'true') {
          this.realComplete();
        }
      }
    );
  }

  getOrder(id: number): void {
    this.orderService.getOrder(id)
      .subscribe(data => {
          const order = data as Order;
          this.order = order;
          this.description.setValue(this.order.description);
          this.description.disable();
          console.log('getOrder in order view:', order);
          this.getCertificates(this.orderId, this.page, this.size);
        },
        error => {
          console.log(error.error.message);
          this.message = error.error.message;
        }
      );
  }

  getCertificates(orderId: number, page: number, size: number): void {
    this.certificateService.getCertificatesOfOrder(orderId, page, size)
      .subscribe(data => {
          this.certificates = data.elements.content as Array<Certificate>;
          this.page = data.elements.page;
          this.last = data.elements.totalPage - 1;
          this.isProcessBar = false;
        },
        error => {
          console.log(error.error.message);
          this.message = error.error.message;
        }
      );
  }

  back(): void {
    this.orderViewStorage.setCurrentOrderId(this.order.id);
    let url = this.tokenStorage.getPreviousUrl().replace('/', '');
    if (url === null || url === undefined || url === 'order-view') {
      url = 'certificates';
    }
    this.router.navigate([url]);
    this.dataModal.changeMessage('false');
  }

  complete(): void {
    if (this.order.completed === true) {
      this.message = 'Order already was completed';
      return;
    }
    this.dataModal.changeMessage(ORDER_VIEW);
  }

  realComplete(): void {
    this.order.completed = true;
    this.updateOrder(this.order);
    this.orderViewStorage.setCurrentOrderId(this.order.id);
  }

  updateOrder(order: Order): void {
    this.orderService.update(order)
      .subscribe(
        data => {
          this.order = data as Order;
          console.log('order view save:', this.order);
        },
        error => {
          console.log(error.error.message);
        }
      );
  }

  view(certificateId: number): void {
    this.certificateStorage.setCurrentCertificate(certificateId);
    this.router.navigate(['certificate']);
  }
}