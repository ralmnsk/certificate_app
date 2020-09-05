import {Injectable} from '@angular/core';
import {Order} from '../orders/order';


@Injectable({
  providedIn: 'root'
})
export class OrderStorageService {
  order: Order;
  certificateIds: Array<number>;

  constructor() {
  }

  setOrder(order: Order): void {
    this.order = order;
  }

  getOrder(): Order {
    return this.order;
  }

  getCertificateIds(): Array<number> {
    return this.certificateIds;
  }

  setCertificateIds(ids: Array<number>): void {
    this.certificateIds = ids;
  }
}
