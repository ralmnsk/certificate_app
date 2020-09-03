import {Component, OnInit} from '@angular/core';
import {UserService} from '../user/user.service';
import {TokenStorageService} from '../auth/token-storage.service';
import {Router} from '@angular/router';
import {DataService} from '../data/data.service';
import {UsersPage} from './users.page';
import {UserInfo} from '../user/info/user.info';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  page = 0;
  private limit = 10;
  usersPage: UsersPage;
  users: Array<UserInfo>;
  first: number;
  private prev: number;
  private next: number;
  last: number;

  constructor(private userService: UserService,
              private tokenStorage: TokenStorageService,
              private router: Router,
              private dataService: DataService) {
  }

  ngOnInit(): void {
    this.getUsers(this.page, this.limit);
  }

  getUsers(page: number, limit: number): void {
    this.disableNavigationButtons();
    this.userService.getAllUsers(page, limit)
      .subscribe(
        data => {
          this.usersPage = data as UsersPage;
          this.first = 0;
          this.page = this.usersPage.page;
          this.last = this.usersPage.totalPage.valueOf() - 1;
          this.users = this.usersPage.elements.content as Array<UserInfo>;
          this.enableNavigationButtons();
        },
        (error) => {
          if (error.error !== undefined) {
            console.log(error.error.message);
          }
          this.enableNavigationButtons();
        }
      );
  }

  toFirstPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.getUsers(0, this.limit);
  }


  toPreviousPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.page = this.page.valueOf() - 1;
    if (this.page < 0) {
      this.page = 0;
    }
    this.getUsers(this.page, this.limit);
  }

  toNextPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.page = this.page.valueOf() + 1;
    if (this.page > this.last) {
      this.page = this.last;
    }
    this.getUsers(this.page, this.limit);
  }

  toLastPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.page = this.last;
    this.getUsers(this.page, this.limit);
  }

  disableNavigationButtons(): void {
    for (let i = 0; i < 4; i++) {
      document.getElementById(i.toString()).className = 'disabled';
    }
    console.log('disabled navigation buttons');
  }

  enableNavigationButtons(): void {
    for (let i = 0; i < 4; i++) {
      document.getElementById(i.toString()).className = 'nav-btn';
    }
    console.log('enable navigation buttons');
  }

  isButtonsDisabled(): boolean {
    if (document.getElementsByClassName('disabled').length > 0) {
      return true;
    }
    return false;
  }
}
