import {Injectable, Output, EventEmitter} from '@angular/core';
import {DataTokenService} from '../data/data-token.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {BehaviorSubject} from 'rxjs';

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
        const current = localStorage.getItem(CURRENT_URL);
        if (current !== null && current !== undefined) {
          localStorage.removeItem(PREVIOUS_URL);
          localStorage.setItem(PREVIOUS_URL, current);
        }
        localStorage.removeItem(CURRENT_URL);
        localStorage.setItem(CURRENT_URL, event.url);
      });
  }

  newMessage(): void {
    this.dataTokenService.changeMessage(this.registerMessage);
  }

  getToken(): string {
    return localStorage.getItem(TOKEN);
  }

  setToken(token: string): void {
    localStorage.removeItem(TOKEN);
    localStorage.setItem(TOKEN, token);
    this.newMessage();
  }

  getName(): string {
    return localStorage.getItem(NAME);
  }

  setName(name: string): void {
    localStorage.removeItem(NAME);
    localStorage.setItem(NAME, name);
  }

  getSurname(): string {
    return localStorage.getItem(SURNAME);
  }

  setSurname(surname: string): void {
    localStorage.removeItem(SURNAME);
    localStorage.setItem(SURNAME, surname);
  }

  getEmail(): string {
    return localStorage.getItem(EMAIL);
  }

  setEmail(email: string): void {
    localStorage.removeItem(EMAIL);
    localStorage.setItem(EMAIL, email);
  }

  getRole(): string {
    return localStorage.getItem(ROLE);
  }

  setRole(role: string): void {
    localStorage.removeItem(ROLE);
    localStorage.setItem(ROLE, role);
  }

  logout(): void {
    localStorage.clear();
  }

  setId(id: string): void {
    localStorage.removeItem(ID);
    localStorage.setItem(ID, id);
  }

  getId(): string {
    return localStorage.getItem(ID);
  }

  getPreviousUrl(): string {
    return localStorage.getItem(PREVIOUS_URL);
  }

  setPreviousUrl(url: string): void {
    localStorage.removeItem(PREVIOUS_URL);
    localStorage.setItem(PREVIOUS_URL, url);
  }
}
