import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {config} from '../config';
import {TokenStorageService} from '../auth/token-storage.service';
import {Certificate} from '../certificates/certificate';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient,
              private tokenStorage: TokenStorageService) {
  }

  getCertificate(id: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log(config.Url + '/certificates/' + id);
    return this.http.get(config.Url + '/certificates/' + id, {headers});
  }

  addTagToCertificate(certificateId: number, tagId: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log(config.Url + '/certificates/' + certificateId + '/tags');
    return this.http.put(config.Url + '/certificates/' + certificateId + '/tags', [tagId], {headers});
  }

  update(certificate: Certificate): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log('certificate update:' + config.Url + '/certificates/' + certificate.id);
    return this.http.put(config.Url + '/certificates/' + certificate.id, certificate, {headers});
  }

  delete(id: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log('certificate update:' + config.Url + '/certificates/' + id);
    return this.http.delete(config.Url + '/certificates/' + id, {headers});
  }

  save(certificate: Certificate): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log('certificate save: ' + config.Url + '/certificates');
    return this.http.post(config.Url + '/certificates', certificate, {headers});
  }
}
