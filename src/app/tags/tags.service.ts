import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {config} from '../config';

@Injectable({
  providedIn: 'root'
})
export class TagsService {

  constructor(private http: HttpClient) { }

  getTags(page?: number, size?: number, name?: string, sort?: string): any {
    console.log(config.Url + '/tags' + '?tagName=' + name
      + '&page=' + page + '&size=' + size + '&sort=' + sort);
    return this.http.get(config.Url + '/tags' + '?tagName=' + name
      + '&page=' + page + '&size=' + size + '&sort=' + sort);
  }
}
