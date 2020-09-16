import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TokenStorageService} from '../auth/token-storage.service';
import {DataTokenService} from '../data/data-token.service';
import {CertificatesService} from '../certificates/certificates.service';
import {Certificate} from '../certificates/certificate';
import {FormControl} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';
import {DataCertificateService} from '../data/data-certificate.service';
import {CertificateStorageService} from '../data/certificate-storage.service';
import {Pagination} from '../certificates/pagination';
import {Tag} from '../tags/tag';
import {TagsService} from '../tags/tags.service';
import {TagStorageService} from '../data/tag-storage.service';
import {DataTagService} from '../data/data-tag.service';
import {UserService} from '../user/user.service';
import {MatMenuTrigger} from '@angular/material/menu';
import {OrderStorageService} from '../data/order-storage.service';
import {DataOrderService} from '../data/data-order.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  private token: string;
  login: string;
  name: string;

  private page: number;
  private size: number;
  tagName: string;
  private certificateName: string;
  private sort: string;
  private last: number;

  private certificates: Array<Certificate>;
  searchControl = new FormControl();
  private message: string;
  private pagination: Pagination;

  tags: Array<Tag>;
  searchTag = new FormControl();

  userLogin: string;
  userRole: string;

  isShowSearchBar: boolean;
  pathsWithoutSearchBar: Set<string>;

  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  purchaseCount: number;

  constructor(private router: Router,
              private tokenStorage: TokenStorageService,
              private dataTokenService: DataTokenService,
              private dataCertificateService: DataCertificateService,
              private certificatesService: CertificatesService,
              private certificateStorage: CertificateStorageService,
              private tagService: TagsService,
              private tagStorageService: TagStorageService,
              private dataTagService: DataTagService,
              private userService: UserService,
              private orderStorage: OrderStorageService,
              private dataOrderService: DataOrderService
  ) {
    this.pathsWithoutSearchBar = new Set<string>();
    this.pathsWithoutSearchBar.add('/user');
    this.pathsWithoutSearchBar.add('/users');
    this.pathsWithoutSearchBar.add('/order');
    this.pathsWithoutSearchBar.add('/orders');
    this.pathsWithoutSearchBar.add('/certificate');
    this.pathsWithoutSearchBar.add('/login');
    this.pathsWithoutSearchBar.add('/order-view');
    this.pathsWithoutSearchBar.add('/order-admin-view');

  }

  ngOnInit(): void {
    this.isShowSearchBar = true;
    this.initPagination();
    this.login = 'Login';
    this.dataCertificateService.currentMessage.subscribe(message => this.message = message);
    this.dataTokenService.currentMessage.subscribe(message => {
      this.token = this.tokenStorage.getToken();
      if (this.token !== undefined && this.token !== null && this.token.length > 5) {
        this.token = message;
        this.login = 'Logout';
        this.initUserInfo();
        console.log('navigation, login user:', this.userLogin);
      }
    });
    this.searchControl.valueChanges
      .pipe(
        debounceTime(2000))
      .subscribe((val) => this.searchByName(val));
    this.searchTag.valueChanges
      .pipe(
        debounceTime(2000))
      .subscribe((val) => this.searchTagByName(val));
    this.dataTagService.currentMessage.subscribe(message => {
      this.tagName = message;
    });
    this.userService.getLoggedIn.subscribe(message => {
      this.initUserInfo();
    });
    this.manageSearchBar();
    this.manageCartMark();
    this.dataOrderService.currentMessage.subscribe(
      message => {
        if (message === 'change-cart-mark') {
          this.manageCartMark();
        }
      }
    );
    this.router.events.subscribe(
      value => {
        // console.log('navigation router event');
        this.manageCartMark();
      }
    );
  }

  initUserInfo(): void {
    // while (this.tokenStorage.getEmail() === null || this.tokenStorage.getEmail() === undefined) {
    this.userLogin = this.tokenStorage.getEmail();
    this.userRole = this.tokenStorage.getRole();
    // }
    // console.log('navigation, userLogin:', this.userLogin);
  }

  initPagination(): void {
    this.page = 0;
    this.size = 10;
    this.tagName = '';
    this.certificateName = '';
    this.sort = 'certificate.name-';
  }

  manageSearchBar(): void {
    const url = this.router.url;
    if (this.pathsWithoutSearchBar.has(url)) {
      this.isShowSearchBar = false;
      return;
    }
    for (const path of this.pathsWithoutSearchBar) {
      if (path.indexOf(url) > 0) {
        this.isShowSearchBar = false;
        return;
      }
    }
    this.isShowSearchBar = true;
  }


  toUserPage(): void {
    this.router.navigate(['user']);
  }


  toUsers(): void {
    this.router.navigate(['users']);
  }

  toCertificates(): void {
    this.router.navigate(['certificates']);
  }


  toOrders(): void {
    this.router.navigate(['orders']);
  }

  logout(): void {
    this.tokenStorage.logout();
    this.token = null;
    this.login = 'Login';
    this.router.navigate(['login']);
    this.userLogin = null;
    this.userRole = null;
  }

  toLogin(): void {
    this.router.navigate(['login']);
  }

  toRegistration(): void {
    this.router.navigate(['register']);
  }

  searchByName(searchValue: string): void {
    this.certificateName = searchValue;
    if (searchValue === undefined || searchValue === null) {
      this.certificateName = '';
    }
    // console.log(searchValue);
    // console.log(this.page, this.size, this.tagName, this.certificateName, this.sort);
    this.certificatesService
      .getCertificates(this.page, this.size, this.tagName, searchValue, this.sort)
      .subscribe(data => {
          this.last = data.totalPage;
          this.page = data.page;
          this.certificates = data.elements.content as Array<Certificate>;
          this.sendPagination();
          this.dataCertificateService.changeMessage(new Date().toString());
          // console.log('navigation certificates:', this.certificates);
        }, error => {
          console.log(error.message);
        }
      );
  }

  sendPagination(): void {
    this.pagination = new Pagination();
    this.pagination.setCertificates(this.certificates);
    this.pagination.setPage(this.page);
    this.pagination.setSize(this.size);
    this.pagination.setTagName(this.tagName);
    this.pagination.setCertificateName(this.certificateName);
    this.pagination.setSort(this.sort);
    this.pagination.setLast(this.last);
    this.certificateStorage.setPagination(this.pagination);
  }

  searchTagByName(searchValue: string): void {
    // console.log(searchValue);
    this.tagName = searchValue;
    this.tagService
      .getTags(0, 7, searchValue, this.sort)
      .subscribe(data => {
          this.tags = data.elements.content as Array<Tag>;
          // console.log('navigation search tags:', this.tags);
        }, error => {
          console.log(error.message);
        }
      );
  }

  setTagName(value: string): void {
    this.tagName = value;
    // console.log('setTagName', this.tagName);
    // this.searchTag.setValue(tag.name);
  }

  createCertificate(): void {
    this.router.navigate(['create-certificate']);
  }

  cart(): void {
    this.router.navigate(['order']);
  }

  displayFn(tag: Tag): string {
    return tag && tag.name ? tag.name : '';
  }

  manageCartMark(): void {
    const set = this.orderStorage.getCertificateIds();
    // const cartToBuy = document.getElementById('cart-to-buy');
    // if (cartToBuy === null || cartToBuy === undefined) {
    //   // console.log('navigation, manage cart mark, cart undefined');
    //   return;
    // }
    // if (set.size === 0) {
    //   cartToBuy.style.backgroundColor = 'white';
    //   // console.log('navigation, manage cart mark, set white');
    //   return;
    // }
    // cartToBuy.style.backgroundColor = 'lightgreen';
    this.purchaseCount = set.size;
    // console.log('navigation, manage cart mark, set green mark of the cart');
  }
}