import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {config} from '../config';
import {TokenStorageService} from '../auth/token-storage.service';
import {Certificate} from '../certificates/certificate';
import {throttleTime} from 'rxjs/operators';

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
    console.log('2.1 certificate service, getCertificate start:', Date.now());
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
    console.log('2.2 certificate service, getCertificate before getting:', Date.now());
    return this.http.get(config.Url + '/certificates/' + id, {headers});
  }

  addTagToCertificate(certificateId: number, tagId: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log(config.Url + '/certificates/' + certificateId + '/tags');
    console.log('add tag to certificate, certificateId:', certificateId, 'tagId:', tagId);
    return this.http.put(config.Url + '/certificates/' + certificateId + '/tags', [tagId], {headers});
  }

  update(certificate: Certificate): any {
    console.log('certificate service:', certificate);
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

  addCertificatesToOrder(id: number, certificateIds: Set<number>): any {
    console.log('certificate service, addCertificatesToOrder');
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    const certificateIdsArray = new Array<number>();
    for (const certificateId of certificateIds) {
      certificateIdsArray.push(certificateId);
    }
    console.log('certificate service, certificateIdsArray:', certificateIdsArray);
    return this.http.put(config.Url + '/orders/' + id + '/certificates', certificateIdsArray, {headers});
  }

  getCertificatesOfOrder(orderId: number, page: number, size: number): any {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json; charset=UTF-8')
      .set('Authorization', this.tokenStorage.getToken());
    // console.log('certificate service, getCertificatesOfOrder:');
    return this.http.get(config.Url + '/orders/' + orderId + '/certificates?page=' + page + '&size=' + size, {headers});
  }
}
