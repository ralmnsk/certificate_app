import {Component, OnInit} from '@angular/core';
import {Certificate} from './certificate';
import {CertificatesService} from './certificates.service';
import {fromEvent, Observable, Subscription} from 'rxjs';
import {DataCertificateService} from '../data/data-certificate.service';
import {Pagination} from './pagination';
import {CertificateStorageService} from '../data/certificate-storage.service';
import {Router} from '@angular/router';
import {DataTagEditService} from '../data/data-tag-edit.service';
import {OrderStorageService} from '../data/order-storage.service';
import {TokenStorageService} from '../auth/token-storage.service';
import {DataOrderService} from '../data/data-order.service';
import {UserService} from '../user/user.service';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.css'],
})
export class CertificatesComponent implements OnInit {

  certificates: Array<Certificate>;
  first: number;
  page: number;
  size: number;
  last: number;
  tagName: string;
  certificateName: string;
  sort: string;

  startWidth: number;
  lastScrollTop: number;
  scale: number;
  currentWidth: number;
  position: number;
  currentCertificatePosition: number;

  resizeObservable: Observable<Event>;
  resizeSubscription: Subscription;

  private message: string;
  cart = 'cart';
  role: string;
  isProcessing: boolean;

  constructor(private certificateService: CertificatesService,
              private dataCertificateService: DataCertificateService,
              private certificateStorage: CertificateStorageService,
              private router: Router,
              private dataTagEditService: DataTagEditService,
              private orderStorage: OrderStorageService,
              private tokenStorage: TokenStorageService,
              private dataOrderService: DataOrderService,
              private userService: UserService
  ) {
    this.scale = 1;
  }

  ngOnInit(): void {
    this.isProcessing = false;
    this.userService.getLoggedIn.subscribe(
      value => {
        this.role = this.tokenStorage.getRole();
      }
    );
    this.startWidth = 1920;
    this.currentWidth = 1920;
    this.lastScrollTop = 0;
    this.scale = 1;
    this.position = 0;
    this.currentCertificatePosition = 0;
    this.page = 0;
    this.size = 10;
    this.tagName = '';
    this.certificateName = '';
    this.sort = 'certificate.name';
    this.dataCertificateService.currentMessage.subscribe(message => {
      this.message = message;
      if (this.certificateStorage.getPagination().getCertificates().length > 0) {
        this.getPagination(this.certificateStorage.getPagination());
      }
      this.markAdded();
    });
    this.resizeObservable = fromEvent(window, 'resize');
    this.resizeSubscription = this.resizeObservable.subscribe(evt => {
      this.currentWidth = window.innerWidth;
      this.scale = Math.round((this.startWidth / this.currentWidth) * 10) / 10;
    });
    this.searchCertificates(this.page, this.size, this.tagName, this.certificateName, this.sort);
    window.addEventListener('scroll', () => {
      this.loadOnScrollDown(this.startWidth, this.currentWidth, this.scale);
    });
    this.role = this.tokenStorage.getRole();
  }

  searchCertificates(page: number, size: number, tagName: string, certificateName: string, sort: string): any {
    this.certificateService.getCertificates(page, size, tagName, certificateName, sort)
      .subscribe(data => {
          this.first = 0;
          this.last = data.totalPage;
          this.page = data.page;
          this.certificates = data.elements.content as Array<Certificate>;
          this.markAdded();
        }, error => {
          console.log(error.message);
          this.message = 'Error happened during certificates search.';
        }
      );
  }

  markAdded(): void {
    const url = this.router.url;
    if (url !== '/certificates') {
      return;
    }
    const set = this.orderStorage.getCertificateIds();
    for (const id of set) {
      const shopCart = document.getElementById(id.toString());
      const span = document.getElementById('cart' + id);
      if (shopCart === undefined || shopCart === null) {
        return;
      }
      if (span === undefined || span === null) {
        return;
      }
      span.innerText = 'Drop from the cart';
      set.add(id);
      shopCart.style.backgroundColor = 'lightgreen';
      this.orderStorage.setCertificateIds(set);
    }
    this.dataOrderService.changeMessage('change-cart-mark');
  }

  loadOnScrollDown(startWidth: number, currentWidth: number, scale: number): void {
    const st: number = window.pageYOffset || document.documentElement.scrollTop;
    const scrollHeight: number = document.documentElement.scrollHeight;
    const clientHeight: number = document.documentElement.clientHeight;
    const width = window.innerWidth;
    if (st.valueOf() >= (scrollHeight.valueOf() - clientHeight.valueOf()) * scale) {
      this.load(this.certificates);
      this.position = st;
    }
  }

  load(certificates: Array<Certificate>): void {
    this.isProcessing = true;
    this.nextPage();
    let downLoadCertificates = new Array<Certificate>();
    this.certificateService.getCertificates(this.page, this.size, this.tagName, this.certificateName, this.sort)
      .subscribe(data => {
          downLoadCertificates = data.elements.content as Array<Certificate>;
          for (let i = 0; i < downLoadCertificates.length; i++) {
            if (certificates !== undefined) {
              certificates.push(downLoadCertificates[i]);
            }
          }
          this.showLoading();
          this.markAdded();
        },
        (error) => {
          console.log(error.message);
          this.message = 'Error happened during certificates loading.';
          this.isProcessing = false;
        }
      );
  }

  showLoading(): void {
    setTimeout(() => {
      this.isProcessing = false;
    }, 3000);
  }

  nextPage(): void {
    this.page = this.page + 1;
    if (this.page > this.last) {
      this.page = this.last;
    }
  }


  toPageTop(): void {
    window.scrollTo(0, 0);
  }

  toPageBottom(): void {
    window.scrollTo(0, this.position);
  }

  getPagination(pagination: Pagination): void {
    this.certificates = pagination.getCertificates();
    this.page = pagination.getPage();
    this.size = pagination.getSize();
    this.last = pagination.getLast();
    this.tagName = pagination.getTagName();
    this.certificateName = pagination.getCertificateName();
    this.sort = pagination.getSort();
  }

  toEdit(value: number): void {
    this.dataTagEditService.changeMessage(value.toString());
    this.certificateStorage.setCurrentCertificate(value);
    this.router.navigate(['certificate']);
  }

  addToCart(id: number): void {
    const set = this.orderStorage.getCertificateIds();
    const shopCart = document.getElementById(id.toString());
    const span = document.getElementById('cart' + id);
    if (shopCart === undefined || shopCart === null) {
      return;
    }
    if (span === undefined || span === null) {
      return;
    }
    if (set.has(id)) {
      span.innerText = 'Add to the cart';
      set.delete(id);
      shopCart.style.backgroundColor = 'white';
    } else if (!set.has(id)) {
      span.innerText = 'Drop from the cart';
      set.add(id);
      shopCart.style.backgroundColor = 'lightgreen';
    }
    this.orderStorage.setCertificateIds(set);
    this.dataOrderService.changeMessage('change-cart-mark');
  }
}
