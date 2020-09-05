import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {config} from '../config';

@Injectable({
  providedIn: 'root'
})
export class CertificatesService {

  constructor(private http: HttpClient) {
  }

  getCertificates(page?: number, size?: number, tagName?: string, certificateName?: string, sort?: string): any {
    if (page === undefined) {
      page = 0;
    }
    if (size === undefined) {
      size = 10;
    }
    // console.log(config.Url + '/certificates' + '?tagName=' + tagName
    //   + '&name=' + certificateName + '&page=' + page + '&size=' + size + '&sort=' + sort);
    return this.http.get(config.Url + '/certificates' + '?tagName=' + tagName
      + '&name=' + certificateName + '&page=' + page + '&size=' + size + '&sort=' + sort);
  }
}
