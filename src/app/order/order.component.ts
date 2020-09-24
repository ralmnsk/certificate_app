import {Component, OnInit} from '@angular/core';
import {Certificate} from '../certificates/certificate';
import {Router} from '@angular/router';
import {Order} from '../orders/order';
import {OrderStorageService} from '../data/order-storage.service';
import {CertificateService} from '../certificate/certificate.service';
import {FormControl, Validators} from '@angular/forms';
import {OrderService} from './order.service';
import {DataModalService} from '../data/data-modal.service';
import {DataOrderService} from '../data/data-order.service';
import {TokenStorageService} from '../auth/token-storage.service';

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
  isProcessSubmit: boolean;
  displayedColumns: string[] = ['Id', 'Name', 'Duration', 'Price', 'Creation', 'Modification', 'Description', 'Remove'];
  windowMessage: string;
  private modalWindowAction: string;

  constructor(private router: Router,
              private orderStorage: OrderStorageService,
              private certificateService: CertificateService,
              private orderService: OrderService,
              private dataModalService: DataModalService,
              private dataOrderService: DataOrderService,
              // private cartCacheService: CartCacheService,
              private tokenStorage: TokenStorageService
  ) {
  }

  ngOnInit(): void {
    this.isProcessSubmit = false;
    this.initOrder();
  }

  initOrder(): void {
    this.isProcessSubmit = false;
    this.isProcessBar = true;
    // this.isModal = false;
    this.certificates = new Array<Certificate>();
    this.description = new FormControl('', Validators.compose([
      Validators.minLength(0),
      Validators.maxLength(999),
      Validators.required
    ]));
    this.getCertificates();
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
        .subscribe(
          data => {
            const certificate = data as Certificate;
            this.certificates.push(certificate);
            counter++;
            if (counter === this.certificateIds.size) {
              this.calculateTotalCost();
              this.isProcessBar = false;
            }
          }, error => {
            this.message = 'Error happened during certificate getting';
            this.isProcessBar = false;
          }
        );
    }
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  cancel(): void {
    this.windowMessage = 'Do you really want to clear the cart?';
    this.modalWindowAction = 'cancel';
    this.showModal();
  }

  realCancel(): void {
    this.orderStorage.cancelOrder();
    this.totalCost = 0;
    this.initOrder();
    this.dataOrderService.changeMessage('change-cart-mark');
  }

  save(): void {
    const idSet = this.orderStorage.getCertificateIds();
    if (idSet === undefined || idSet === null || idSet.size === 0) {
      this.message = 'There are no certificates in the order. The order can not be submitted';
      return;
    }
    this.windowMessage = 'Do you really want to submit?';
    this.modalWindowAction = 'save';
    this.showModal();
  }

  showModal(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'block';
  }

  hide(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'none';
  }

  no(): void {
    this.hide();
  }

  yes(): void {
    switch (this.modalWindowAction) {
      case 'cancel': {
        this.realCancel();
        this.hide();
        break;
      }
      case 'save': {
        this.realSave();
        this.hide();
        break;
      }
    }
  }

  realSave(): void {
    // this.dataModalService.changeMessage('nothing');
    this.no();
    this.isProcessSubmit = true;
    const order = this.orderStorage.getOrder();
    this.certificateIds = this.orderStorage.getCertificateIds();
    if (this.certificateIds.size === 0) {
      this.message = 'There are no certificates to save';
      return;
    }
    order.description = this.description.value;
    this.orderService.save(order)
      .subscribe(data => {
          const savedOrder = data as Order;
          this.certificateService.addCertificatesToOrder(savedOrder.id, this.certificateIds)
            .subscribe(result => {
                this.isProcessSubmit = false;
                this.router.navigate(['orders']);
                this.orderService.getOrder(savedOrder.id);
                this.realCancel();
                this.certificates = new Array<Certificate>();
                this.certificateIds.clear();
              }, error => {
                console.log(error.error.message);
                this.message = 'Error happened during certificates saving in order';
                this.isProcessSubmit = false;
              }
            );
        }, error => {
          console.log(error.message);
          this.message = 'Error happened during order saving';
          const id = this.tokenStorage.getId();
          if (id === null || id === undefined || Number(id) === 0) {
            this.message = 'The user did not log in. ';
          }
          this.isProcessSubmit = false;
        }
      );
  }

  calculateTotalCost(): void {
    this.totalCost = 0.00;
    for (const certificate of this.certificates) {
      this.totalCost = this.totalCost + certificate.price;
    }
  }

  remove(id: number): void {
    this.certificateIds.delete(id);
    const certificates = new Array<Certificate>();
    this.orderStorage.setCertificateIds(this.certificateIds);
    for (let i = 0; i < this.certificates.length; i++) {
      if (this.certificates[i].id !== id) {
        certificates.push(this.certificates[i]);
      }
    }
    this.certificates = certificates;
    this.calculateTotalCost();
    this.dataOrderService.changeMessage('change-cart-mark');
  }
}
