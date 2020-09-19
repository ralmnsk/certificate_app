import {Injectable} from '@angular/core';

export const ORDER_ID = 'OrderId';

@Injectable({
  providedIn: 'root'
})
export class OrderViewStorageService {

  constructor() {
  }

  getCurrentOrderId(): number {
    return Number(localStorage.getItem(ORDER_ID));
  }

  setCurrentOrderId(value: number): void {
    localStorage.removeItem(ORDER_ID);
    localStorage.setItem(ORDER_ID, value.toString());
  }

  clear(): void {
    localStorage.removeItem('OrderId');
  }
}
