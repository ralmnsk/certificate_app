import {Injectable} from '@angular/core';
import {Order} from '../orders/order';

export const ID = 'orderId';
export const DESCRIPTION = 'orderDescription';
export const TOTAL_COST = 'orderTotalCost';
export const CREATED = 'orderCreated';
export const COMPLETED = 'orderCompleted';
export const CERTIFICATE_ID = 'certificateId';
export const CERTIFICATE_COUNT = 'certificateCount';

@Injectable({
  providedIn: 'root'
})

export class OrderStorageService {
  certificates: Set<number>;

  constructor() {
    this.certificates = new Set<number>();
  }

  setOrder(order: Order): void {
    window.sessionStorage.removeItem(ID);
    window.sessionStorage.removeItem(DESCRIPTION);
    window.sessionStorage.removeItem(TOTAL_COST);
    window.sessionStorage.removeItem(CREATED);
    window.sessionStorage.removeItem(COMPLETED);
    window.sessionStorage.setItem(ID, order.id.toString());
    window.sessionStorage.setItem(DESCRIPTION, order.description);
    window.sessionStorage.setItem(TOTAL_COST, order.totalCost.toString());
    window.sessionStorage.setItem(CREATED, order.created.toString());
    window.sessionStorage.setItem(COMPLETED, String(order.completed));
  }

  getOrder(): Order {
    const order = new Order();
    order.id = Number(window.sessionStorage.getItem(ID));
    order.description = window.sessionStorage.getItem(DESCRIPTION);
    order.totalCost = Number(window.sessionStorage.getItem(TOTAL_COST));
    order.created = new Date(window.sessionStorage.getItem(CREATED));
    order.completed = Boolean(window.sessionStorage.getItem(COMPLETED));
    return order;
  }

  getCertificateIds(): Set<number> {
    this.certificates = new Set<number>();
    const count = Number(window.sessionStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (count + 1); i++) {
      const id = Number(window.sessionStorage.getItem(CERTIFICATE_ID + i));
      if (id !== 0) {
        this.certificates.add(id);
      }
    }
    return this.certificates;
  }

  setCertificateIds(set: Set<number>): void {
    console.log('order-storage, set:', set);
    const count = set.size;
    let num = 0;

    const oldCount = Number(window.sessionStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (oldCount + 1); i++) {
      window.sessionStorage.removeItem(CERTIFICATE_ID + i);
    }
    window.sessionStorage.removeItem(CERTIFICATE_COUNT);

    for (const id of set) {
      window.sessionStorage.removeItem(CERTIFICATE_ID + num);
      window.sessionStorage.setItem(CERTIFICATE_ID + num, id.toString());
      num++;
    }
    window.sessionStorage.setItem(CERTIFICATE_COUNT, count.toString());
  }

  cancelOrder(): void {
    window.sessionStorage.removeItem(ID);
    window.sessionStorage.removeItem(DESCRIPTION);
    window.sessionStorage.removeItem(TOTAL_COST);
    window.sessionStorage.removeItem(CREATED);
    window.sessionStorage.removeItem(COMPLETED);

    const count = Number(window.sessionStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (count + 1); i++) {
      window.sessionStorage.removeItem(CERTIFICATE_ID + i);
    }
    window.sessionStorage.removeItem(CERTIFICATE_COUNT);
  }
}
