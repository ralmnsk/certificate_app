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
    this.pathsWithoutSearchBar.add('/create-certificate');
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
        this.manageCartMark();
      }
    );
  }

  initUserInfo(): void {
    this.userLogin = this.tokenStorage.getEmail();
    this.userRole = this.tokenStorage.getRole();
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
      if (url.indexOf(path) >= 0) {
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
    this.purchaseCount = null;
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
    this.certificatesService
      .getCertificates(this.page, this.size, this.tagName, searchValue, this.sort)
      .subscribe(data => {
          this.last = data.totalPage;
          this.page = data.page;
          this.certificates = data.elements.content as Array<Certificate>;
          this.sendPagination();
          this.dataCertificateService.changeMessage(new Date().toString());
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
    this.tagName = searchValue;
    this.tagService
      .getTags(0, 7, searchValue, this.sort)
      .subscribe(data => {
          this.tags = data.elements.content as Array<Tag>;
        }, error => {
          console.log(error.message);
        }
      );
  }

  setTagName(value: string): void {
    this.tagName = value;
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
    this.purchaseCount = set.size;
  }
}
