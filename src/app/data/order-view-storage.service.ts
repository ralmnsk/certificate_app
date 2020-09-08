import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OrderViewStorageService {

  constructor() { }

  getCurrentOrderId(): number {
    return Number(window.sessionStorage.getItem('OrderId'));
  }

  setCurrentOrderId(value: number): void {
    window.sessionStorage.removeItem('OrderId');
    window.sessionStorage.setItem('OrderId', value.toString());
  }
}
