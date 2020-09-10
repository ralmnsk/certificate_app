import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenStorageService} from '../auth/token-storage.service';
import {User} from './user';
import {UpdateUser} from './update.user';
import {config} from '../config';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private user: User;
  private url: string;

  constructor(private http: HttpClient,
              private tokenStorage: TokenStorageService) {
  }

  @Output() getLoggedIn: EventEmitter<any> = new EventEmitter();

  setUserInStorage(url: string): any {
    const headers = new HttpHeaders()
      .set('Access-Control-Allow-Headers', 'Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept')
      .set('Access-Control-Allow-Origin', url)
      .set('Access-Control-Allow-Methods', 'GET,PUT,POST,PATCH,DELETE')
      .set('Content-Type', 'application/json; charset=UTF-8')
      // .set('Access-Control-Request-Method', 'GET,PUT,POST,DELETE')
      .set('Authorization', this.tokenStorage.getToken());
    console.log('userService url:', url, ',getUserDataByUrl: token:', this.tokenStorage.getToken());
    return this.http.get<User>(url, {headers})
      .subscribe(result => {
        console.log('userService result:', result);
        this.tokenStorage.setId(result.id.toString());
        this.tokenStorage.setSurname(result.surname);
        this.tokenStorage.setName(result.name);
        this.tokenStorage.setEmail(result.email);
        this.tokenStorage.setRole(result.role);
        this.getLoggedIn.emit('loggedIn');
      }, error => {
        console.log('userService error:', error);
        return 'error';
      })
      ;
  }

  update(updateUser: UpdateUser): any {
    this.url = config.Url + '/users/' + this.tokenStorage.getId();
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.put<User>(this.url, updateUser, {headers})
      .subscribe(result => {
        console.log('userService result:', result);
        this.setUserInStorage(this.url);
      }, error => {
        console.log('userService error:', error);
        return 'error';
      })
      ;
  }

  getAllUsers(page?: number, size?: number): any {
    if (page === undefined) {
      page = 0;
    }
    if (size === undefined) {
      size = 10;
    }
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.get(config.Url + '/users' + '?page=' + page + '&size=' + size, {headers});
  }

  getAllUsersBySurname(surname?: string, page?: number, size?: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());

    return this.http.get(config.Url + '/users?surname=' + surname + '&page=' + page + '&size=' + size, {headers});
  }
}
