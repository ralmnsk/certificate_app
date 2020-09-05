import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {config} from '../config';
import {TokenStorageService} from '../auth/token-storage.service';
import {Tag} from './tag';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TagsService {

  constructor(private http: HttpClient,
              private tokenStorage: TokenStorageService
  ) {
  }

  getTags(page?: number, size?: number, name?: string, sort?: string): any {
    // console.log(config.Url + '/tags' + '?tagName=' + name
    //   + '&page=' + page + '&size=' + size + '&sort=' + sort);
    return this.http.get(config.Url + '/tags' + '?tagName=' + name
      + '&page=' + page + '&size=' + size + '&sort=' + sort);
  }

  getTagsByCertificateId(value: number): any {
    return this.http.get(config.Url + '/certificates/' + value + '/tags?page=0&size=100');
  }

  create(value: string): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    let tag = new Tag();
    tag.name = value;
    // console.log('tag service, create:', config.Url + '/tags');
    return this.http.post(config.Url + '/tags', tag, {headers});
  }

  remove(certificateId: number, tagId: number): Observable<any> {
    // const headers = new HttpHeaders()
    //   .set('Content-Type', 'application/json; charset=UTF-8')
    //   .set('Authorization', this.tokenStorage.getToken());
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': this.tokenStorage.getToken()
      }),
      body: [tagId],
    };
    // console.log('remove tag:', config.Url + '/certificates/' + certificateId + '/tags');
    return this.http.delete(config.Url + '/certificates/' + certificateId + '/tags', options);
  }
}
