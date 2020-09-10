import {Injectable} from '@angular/core';
import {DataTokenService} from '../data/data-token.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

export const TOKEN = 'Token';
export const SURNAME = 'Surname';
export const NAME = 'Name';
export const EMAIL = 'Email';
export const ROLE = 'Role';
export const ID = 'Id';
export const PREVIOUS_URL = 'Previous';
export const CURRENT_URL = 'Current';


@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  registerMessage: string;

  constructor(private dataTokenService: DataTokenService,
              private router: Router
  ) {
    router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        console.log('prev:', event.url);
        const current = window.sessionStorage.getItem(CURRENT_URL);
        if (current !== null && current !== undefined) {
          window.sessionStorage.removeItem(PREVIOUS_URL);
          window.sessionStorage.setItem(PREVIOUS_URL, current);
        }
        window.sessionStorage.removeItem(CURRENT_URL);
        window.sessionStorage.setItem(CURRENT_URL, event.url);
      });
  }

  newMessage(): void {
    this.dataTokenService.changeMessage(this.registerMessage);
  }

  getToken(): string {
    return window.sessionStorage.getItem(TOKEN);
  }

  setToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN);
    window.sessionStorage.setItem(TOKEN, token);
    this.newMessage();
  }

  getName(): string {
    return window.sessionStorage.getItem(NAME);
  }

  setName(name: string): void {
    window.sessionStorage.removeItem(NAME);
    window.sessionStorage.setItem(NAME, name);
  }

  getSurname(): string {
    return window.sessionStorage.getItem(SURNAME);
  }

  setSurname(surname: string): void {
    window.sessionStorage.removeItem(SURNAME);
    window.sessionStorage.setItem(SURNAME, surname);
  }

  getEmail(): string {
    return window.sessionStorage.getItem(EMAIL);
  }

  setEmail(email: string): void {
    window.sessionStorage.removeItem(EMAIL);
    window.sessionStorage.setItem(EMAIL, email);
  }

  getRole(): string {
    return window.sessionStorage.getItem(ROLE);
  }

  setRole(role: string): void {
    window.sessionStorage.removeItem(ROLE);
    window.sessionStorage.setItem(ROLE, role);
  }

  logout(): void {
    window.sessionStorage.clear();
  }

  setId(id: string): void {
    window.sessionStorage.removeItem(ID);
    window.sessionStorage.setItem(ID, id);
  }

  getId(): string {
    return window.sessionStorage.getItem(ID);
  }

  getPreviousUrl(): string {
    return window.sessionStorage.getItem(PREVIOUS_URL);
  }

  setPreviousUrl(url: string): void {
    window.sessionStorage.removeItem(PREVIOUS_URL);
    window.sessionStorage.setItem(PREVIOUS_URL, url);
  }
}
