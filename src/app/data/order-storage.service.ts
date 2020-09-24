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
    localStorage.removeItem(ID);
    localStorage.removeItem(DESCRIPTION);
    localStorage.removeItem(TOTAL_COST);
    localStorage.removeItem(CREATED);
    localStorage.removeItem(COMPLETED);
    localStorage.setItem(ID, order.id.toString());
    localStorage.setItem(DESCRIPTION, order.description);
    localStorage.setItem(TOTAL_COST, order.totalCost.toString());
    localStorage.setItem(CREATED, order.created.toString());
    localStorage.setItem(COMPLETED, String(order.completed));
  }

  getOrder(): Order {
    const order = new Order();
    order.id = Number(localStorage.getItem(ID));
    order.description = localStorage.getItem(DESCRIPTION);
    order.totalCost = Number(localStorage.getItem(TOTAL_COST));
    order.created = new Date(localStorage.getItem(CREATED));
    order.completed = Boolean(localStorage.getItem(COMPLETED));
    return order;
  }

  getCertificateIds(): Set<number> {
    this.certificates = new Set<number>();
    const count = Number(localStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (count + 1); i++) {
      const id = Number(localStorage.getItem(CERTIFICATE_ID + i));
      if (id !== 0) {
        this.certificates.add(id);
      }
    }
    return this.certificates;
  }

  setCertificateIds(set: Set<number>): void {
    const count = set.size;
    let num = 0;

    const oldCount = Number(localStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (oldCount + 1); i++) {
      localStorage.removeItem(CERTIFICATE_ID + i);
    }
    localStorage.removeItem(CERTIFICATE_COUNT);

    for (const id of set) {
      localStorage.removeItem(CERTIFICATE_ID + num);
      localStorage.setItem(CERTIFICATE_ID + num, id.toString());
      num++;
    }
    localStorage.setItem(CERTIFICATE_COUNT, count.toString());
  }

  cancelOrder(): void {
    localStorage.removeItem(ID);
    localStorage.removeItem(DESCRIPTION);
    localStorage.removeItem(TOTAL_COST);
    localStorage.removeItem(CREATED);
    localStorage.removeItem(COMPLETED);

    const count = Number(localStorage.getItem(CERTIFICATE_COUNT));
    for (let i = 0; i < (count + 1); i++) {
      localStorage.removeItem(CERTIFICATE_ID + i);
    }
    localStorage.removeItem(CERTIFICATE_COUNT);
  }
}
