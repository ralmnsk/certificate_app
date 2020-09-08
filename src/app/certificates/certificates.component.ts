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

  constructor(private certificateService: CertificatesService,
              private dataCertificateService: DataCertificateService,
              private certificateStorage: CertificateStorageService,
              private router: Router,
              private dataTagEditService: DataTagEditService,
              private orderStorage: OrderStorageService
  ) {
    this.scale = 1;
  }

  ngOnInit(): void {
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
      // console.log('certificates component, pagination:', this.certificateStorage.getPagination());
      if (this.certificateStorage.getPagination().getCertificates().length > 0) {
        this.getPagination(this.certificateStorage.getPagination());
      }
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

  }

  searchCertificates(page: number, size: number, tagName: string, certificateName: string, sort: string): any {
    this.certificateService.getCertificates(page, size, tagName, certificateName, sort)
      .subscribe(data => {
          this.first = 0;
          this.last = data.totalPage;
          this.page = data.page;
          this.certificates = data.elements.content as Array<Certificate>;
        }, error => {
          console.log(error.message);
          this.message = 'Error happened during certficates search.';
        }
      );
  }

  loadOnScrollDown(startWidth: number, currentWidth: number, scale: number): void {
    const st: number = window.pageYOffset || document.documentElement.scrollTop;
    const scrollHeight: number = document.documentElement.scrollHeight;
    const clientHeight: number = document.documentElement.clientHeight;
    const width = window.innerWidth;
    if (st.valueOf() >= (scrollHeight.valueOf() - clientHeight.valueOf()) * scale) {
      this.load(this.certificates);
      this.position = st;
    } else {
      // console.log('up');
    }
  }

  load(certificates: Array<Certificate>): void {
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
        },
        (error) => {
          console.log(error.message);
          this.message = 'Error happened during certificates loading.';
        }
      );
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
    this.router.navigate(['certificate']);
    this.dataTagEditService.changeMessage(value.toString());
    this.certificateStorage.setCurrentCertificate(value);
  }

  addToCart(id: number): void {
    const set = this.orderStorage.getCertificateIds();
    console.log('certificates component, set:', set);
    set.add(id);
    this.orderStorage.setCertificateIds(set);
  }
}
