import {Component, OnInit} from '@angular/core';
import {Certificate} from '../certificates/certificate';
import {Router} from '@angular/router';
import {Order} from '../orders/order';
import {OrderStorageService} from '../data/order-storage.service';
import {CertificateService} from '../certificate/certificate.service';
import {FormControl, Validators} from '@angular/forms';
import {OrderService} from './order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  certificates: Set<Certificate>;
  certificateIds: Set<number>;
  order: Order;
  message: string;
  totalCost: number;

  description: FormControl;

  constructor(private router: Router,
              private orderStorage: OrderStorageService,
              private certificateService: CertificateService,
              private orderService: OrderService
  ) {
  }

  ngOnInit(): void {
    this.certificates = new Set<Certificate>();
    this.description = new FormControl('', Validators.compose([
      Validators.minLength(0),
      Validators.maxLength(999)
      // Validators.required
    ]));
    this.getCertificates();
    console.log('order component, certificate ids from  storage:', this.orderStorage.getCertificateIds());
  }


  getCertificates(): void {
    this.certificateIds = this.orderStorage.getCertificateIds();
    for (const id of this.certificateIds) {
      this.certificateService.getCertificate(id)
        .subscribe(data => {
            // console.log('order component, id:', id);
            const certificate = data as Certificate;
            this.certificates.add(certificate);
            this.calculateTotalCost();
          }, error => {
            console.log(error.message);
            this.message = 'Order component getting certificates error';
          }
        );
    }
  }


  back(): void {
    this.router.navigate(['certificates']);
  }

  cancel(): void {
    this.orderStorage.cancelOrder();
    this.ngOnInit();
    // this.router.navigate(['order']);
  }

  save(): void {
    this.order = this.orderStorage.getOrder();
    this.certificateIds = this.orderStorage.getCertificateIds();
    this.order.description = this.description.value;
    this.orderService.save(this.order)
      .subscribe(data => {
          this.order = data as Order;
          console.log('order component, save order:', this.order);
          this.certificateService.addCertificatesToOrder(this.order.id, this.certificateIds)
            .subscribe(result => {
                console.log(result.elements.content);
                this.orderStorage.cancelOrder();
                this.router.navigate(['orders']);
              }, error => {
                console.log(error);
                this.message = 'Error happened during certificates saving in order';
              }
            );
        }, error => {
          console.log(error.message);
          this.message = 'Error happened during order saving';
        }
      );
  }

  calculateTotalCost(): void {
    this.totalCost = 0.00;
    console.log('certificates:', this.certificates);
    for (const certificate of this.certificates) {
      this.totalCost = this.totalCost + certificate.price;
      console.log('total cost, price:', certificate.price);
    }
  }
}
