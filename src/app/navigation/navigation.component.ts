import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TokenStorageService} from '../auth/token-storage.service';
import {DataTokenService} from '../data/data-token.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  private token: string;
  login: string;

  constructor(private router: Router,
              private tokenStorage: TokenStorageService,
              private dataTokenService: DataTokenService
  ) {
  }

  ngOnInit(): void {
    this.login = 'Login';
    this.dataTokenService.currentMessage.subscribe(message => {
      this.token = this.tokenStorage.getToken();
      if (this.token !== undefined && this.token !== null && this.token.length > 5) {
        this.token = message;
        this.login = 'Logout';
        console.log('navigation, message from dataTokenService:', message);
      }
    });
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
  }

  toLogin(): void {
    this.router.navigate(['login']);
  }

  toRegistration(): void {
    this.router.navigate(['register']);
  }
}
