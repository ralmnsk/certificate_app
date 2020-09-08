import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {config} from '../config';
import {TokenStorageService} from '../auth/token-storage.service';
import {Order} from '../orders/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http: HttpClient,
              private tokenStorage: TokenStorageService
  ) {
  }

  getOrders(userId: number, page?: number, size?: number): any {
    if (page === undefined) {
      page = 0;
    }
    if (size === undefined) {
      size = 10;
    }
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log(config.Url + '/users/' + userId + '/orders?page=' + page + '&size=' + size + '&sort=orders.id+');
    return this.http.get(config.Url + '/users/' + userId + '/orders?page=' + page + '&size=' + size + '&sort=orders.id+', {headers});
  }

  getOrder(orderId: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log(config.Url + '/orders/' + orderId);
    return this.http.get(config.Url + '/orders/' + orderId, {headers});
  }

  save(order: Order): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log('order service save, order:', order);
    return this.http.post(config.Url + '/orders', {description: order.description}, {headers});
  }

  update(order: Order): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    console.log('order service save, order:', order);
    return this.http.put(config.Url + '/orders/' + order.id, {
      description: order.description,
      completed: order.completed
    }, {headers});
  }
}
