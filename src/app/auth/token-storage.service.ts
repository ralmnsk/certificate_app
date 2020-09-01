import {Injectable} from '@angular/core';
import {DataTokenService} from '../data/data-token.service';

export const TOKEN = 'Token';
export const SURNAME = 'Surname';
export const NAME = 'Name';
export const EMAIL = 'Email';
export const ROLE = 'Role';
export const ID = 'Id';


@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  registerMessage: string;

  constructor(private dataTokenService: DataTokenService) {
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
}
