import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {config} from '../config';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) {
  }


  register(data): Observable<any> {
    const headers = {
      'Content-type': 'application/json'
    };
    return this.http.post(`${config.Url}/register`, data, {headers});
  }
}
