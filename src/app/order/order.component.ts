import {Component, OnInit} from '@angular/core';
import {Certificate} from '../certificates/certificate';
import {Router} from '@angular/router';
import {Order} from '../orders/order';
import {OrderStorageService} from '../data/order-storage.service';
import {CertificateService} from '../certificate/certificate.service';
import {FormControl, Validators} from '@angular/forms';
import {OrderService} from './order.service';
import {DataModalService} from '../data/data-modal.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  certificates: Array<Certificate>;
  certificateIds: Set<number>;
  order: Order;
  message: string;
  totalCost: number;

  description: FormControl;
  isProcessBar: boolean;
  displayedColumns: string[] = ['Id', 'Name', 'Duration', 'Price', 'Creation', 'Modification', 'Description', 'Remove'];
  // isModal = false;

  constructor(private router: Router,
              private orderStorage: OrderStorageService,
              private certificateService: CertificateService,
              private orderService: OrderService,
              private dataModalService: DataModalService
  ) {
  }

  ngOnInit(): void {
    this.dataModalService.changeMessage('false');
    this.initOrder();
    this.dataModalService.backMessage
      .subscribe(data => {
        if (data === 'true') {
          this.realCancel();
          this.router.navigate(['order']);
          this.initOrder();
        }
      });
  }

  initOrder(): void {
    this.isProcessBar = true;
    // this.isModal = false;
    this.certificates = new Array<Certificate>();
    this.description = new FormControl('', Validators.compose([
      Validators.minLength(0),
      Validators.maxLength(999),
      Validators.required
    ]));
    this.getCertificates();
    // console.log('initOrder, isModal:', this.isModal);
  }

  getCertificates(): void {
    this.certificateIds = this.orderStorage.getCertificateIds();
    const size = this.certificateIds.size;
    if (size === 0) {
      this.isProcessBar = false;
    }
    let counter = 0;
    for (const id of this.certificateIds) {
      this.certificateService.getCertificate(id)
        .subscribe(data => {
            // console.log('order component, id:', id);
            const certificate = data as Certificate;
            this.certificates.push(certificate);
            counter++;
            if (counter === size) {
              this.calculateTotalCost();
              this.isProcessBar = false;
            }
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
    this.dataModalService.changeMessage('order');
  }


  realCancel(): void {
    this.orderStorage.cancelOrder();
    this.totalCost = 0;
  }

  save(): void {
    this.order = this.orderStorage.getOrder();
    this.certificateIds = this.orderStorage.getCertificateIds();
    if (this.certificateIds.size === 0) {
      this.message = 'There are no certificates to save';
      return;
    }
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
                this.orderService.getOrder(this.order.id);
              }, error => {
                console.log(error.error.message);
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

  remove(id: number): void {
    this.certificateIds.delete(id);
    const certificates = new Array<Certificate>();
    this.orderStorage.setCertificateIds(this.certificateIds);
    // let found = null;
    for (let i = 0; i < this.certificates.length; i++) {
      if (this.certificates[i].id !== id) {
        certificates.push(this.certificates[i]);
      }
    }
    this.certificates = certificates;
    this.calculateTotalCost();
  }
}
