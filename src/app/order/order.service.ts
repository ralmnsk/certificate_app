import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {config} from '../config';
import {TokenStorageService} from '../auth/token-storage.service';

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
    console.log(config.Url + '/users/' + userId + '/orders?page=' + page + '&size=' + size + '&sort=orders.id+');
    return this.http.get(config.Url + '/users/' + userId + '/orders?page=' + page + '&size=' + size + '&sort=orders.id+', {headers});
  }
}
