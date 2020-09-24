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
    if (id === null || id === undefined || id === 0 ){
      return new Error('Incompatible type of the certificate id');
    }
    const token = this.tokenStorage.getToken();
    let headers;
    if (token === null || token === undefined) {
      headers = new HttpHeaders()
        .set('Content-Type', 'application/json; charset=UTF-8');
    } else {
      headers = new HttpHeaders()
        .set('Content-Type', 'application/json; charset=UTF-8')
        .set('Authorization', this.tokenStorage.getToken());
    }
    return this.http.get(config.Url + '/certificates/' + id, {headers});
  }

  addTagToCertificate(certificateId: number, tagId: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.put(config.Url + '/certificates/' + certificateId + '/tags', [tagId], {headers});
  }

  update(certificate: Certificate): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.put(config.Url + '/certificates/' + certificate.id, certificate, {headers});
  }

  delete(id: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.delete(config.Url + '/certificates/' + id, {headers});
  }

  save(certificate: Certificate): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.post(config.Url + '/certificates', certificate, {headers});
  }

  addCertificatesToOrder(id: number, certificateIds: Set<number>): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    const certificateIdsArray = new Array<number>();
    for (const certificateId of certificateIds) {
      certificateIdsArray.push(certificateId);
    }
    return this.http.put(config.Url + '/orders/' + id + '/certificates', certificateIdsArray, {headers});
  }

  getCertificatesOfOrder(orderId: number, page: number, size: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    return this.http.get(config.Url + '/orders/' + orderId + '/certificates?page=' + page + '&size=' + size, {headers});
  }
}
