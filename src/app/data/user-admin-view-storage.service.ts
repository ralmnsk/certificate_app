import {EventEmitter, Injectable, Output} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAdminViewStorageService {

  constructor() {
  }

  @Output() getChangedUserView: EventEmitter<any> = new EventEmitter();

  getCurrentUserId(): number {
    return Number(window.sessionStorage.getItem('userViewId'));
  }

  setCurrentUserId(value: number): void {
    window.sessionStorage.removeItem('userViewId');
    window.sessionStorage.setItem('userViewId', value.toString());
    this.getChangedUserView.emit('getChangedUserView');
  }

  setSurname(surname: string): void {
    window.sessionStorage.removeItem('SurnameViewId');
    window.sessionStorage.setItem('SurnameViewId', surname);
    this.getChangedUserView.emit('getChangedUserView');
  }

  getSurname(): string {
    return window.sessionStorage.getItem('SurnameViewId');
  }

  setName(name: string): void {
    window.sessionStorage.removeItem('NameViewId');
    window.sessionStorage.setItem('NameViewId', name);
    this.getChangedUserView.emit('getChangedUserView');
  }

  getName(): string {
    return window.sessionStorage.getItem('NameViewId');
  }
}
