import {Component, OnInit} from '@angular/core';
import {UserService} from '../user/user.service';
import {Router} from '@angular/router';
import {UsersPage} from './users.page';
import {UserInfo} from '../user/info/user.info';
import {FormControl} from '@angular/forms';
import {UserAdminViewStorageService} from '../data/user-admin-view-storage.service';

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
  prev: number;
  next: number;
  last: number;

  userSendId: number;

  message: string;
  findUserControl = new FormControl('');
  isProcessBar: boolean;

  displayedColumns: string[] = ['View', 'Id', 'Surname', 'Name'];
  surname: string;

  constructor(private userService: UserService,
              private router: Router,
              private userAdminViewStorageService: UserAdminViewStorageService
  ) {
  }

  ngOnInit(): void {
    this.surname = '';
    this.getUsers(this.page, this.limit);
  }

  getUsers(page: number, limit: number): void {
    this.isProcessBar = true;
    this.disableNavigationButtons();
    this.userService.getAllUsersBySurname(this.surname, page, limit)
      .subscribe(
        data => {
          this.usersPage = data as UsersPage;
          this.first = 0;
          this.page = this.usersPage.page;
          this.last = this.usersPage.totalPage.valueOf() - 1;
          this.users = this.usersPage.elements.content as Array<UserInfo>;
          this.enableNavigationButtons();
          this.isProcessBar = false;
        },
        (error) => {
          if (error.error !== undefined) {
            console.log(error.error.message);
            this.isProcessBar = false;
            this.enableNavigationButtons();
          }
          this.enableNavigationButtons();
        }
      );
  }

  toFirstPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    if (this.page === this.first) {
      return;
    }
    this.getUsers(0, this.limit);
  }


  toPreviousPage(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    if (this.page === this.first) {
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
    if (this.page === this.last) {
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
    if (this.page === this.last) {
      return;
    }
    this.page = this.last;
    this.getUsers(this.page, this.limit);
  }

  disableNavigationButtons(): void {
    for (let i = 0; i < 4; i++) {
      if (document.getElementById(i.toString()) !== null && document.getElementById(i.toString()) !== undefined) {
        document.getElementById(i.toString()).className = 'disabled';
      }
    }
    console.log('disabled navigation buttons');
  }

  enableNavigationButtons(): void {
    for (let i = 0; i < 4; i++) {
      if (document.getElementById(i.toString()) !== null && document.getElementById(i.toString()) !== undefined) {
        document.getElementById(i.toString()).className = 'nav-btn';
      }
    }
    console.log('enable navigation buttons');
  }

  isButtonsDisabled(): boolean {
    if (document.getElementsByClassName('disabled').length > 0) {
      return true;
    }
    return false;
  }

  searchUser(): void {
    const surname = this.findUserControl.value;
    this.surname = surname;
    if (surname === '' || surname === null || surname === undefined) {
      this.surname = '';
    }
    this.isProcessBar = true;
    this.disableNavigationButtons();
    this.userService.getAllUsersBySurname(surname, 0, this.limit)
      .subscribe(
        data => {
          this.usersPage = data as UsersPage;
          this.first = 0;
          this.page = this.usersPage.page;
          this.last = this.usersPage.totalPage.valueOf() - 1;
          this.users = this.usersPage.elements.content as Array<UserInfo>;
          this.enableNavigationButtons();
          this.isProcessBar = false;
        },
        (error) => {
          this.message = error.error.message;
          this.enableNavigationButtons();
          this.isProcessBar = false;
        }
      );
  }

  viewOrders(userId: number, surname: string, name: string): void {
    this.userSendId = userId;
    this.userAdminViewStorageService.setCurrentUserId(userId);
    this.userAdminViewStorageService.setSurname(surname);
    this.userAdminViewStorageService.setName(name);
    this.router.navigate(['order-admin-view']);
  }
}
