import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {config} from '../config';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(data): Observable<any> {
    const headers = {
      'Content-type': 'application/json'
    };
    return this.http.post(`${config.Url}/login`, data, {headers});
  }
}
