import {EventEmitter, Injectable, Output} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAdminViewStorageService {

  constructor() {
  }

  @Output() getChangedUserView: EventEmitter<any> = new EventEmitter();

  getCurrentUserId(): number {
    return Number(localStorage.getItem('userViewId'));
  }

  setCurrentUserId(value: number): void {
    localStorage.removeItem('userViewId');
    localStorage.setItem('userViewId', value.toString());
    this.getChangedUserView.emit('getChangedUserView');
  }

  setSurname(surname: string): void {
    localStorage.removeItem('SurnameViewId');
    localStorage.setItem('SurnameViewId', surname);
    this.getChangedUserView.emit('getChangedUserView');
  }

  getSurname(): string {
    return localStorage.getItem('SurnameViewId');
  }

  setName(name: string): void {
    localStorage.removeItem('NameViewId');
    localStorage.setItem('NameViewId', name);
    this.getChangedUserView.emit('getChangedUserView');
  }

  getName(): string {
    return localStorage.getItem('NameViewId');
  }
}
