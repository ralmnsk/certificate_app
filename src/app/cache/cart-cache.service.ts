import {Injectable} from '@angular/core';
import {Certificate} from '../certificates/certificate';
import {CertificateService} from '../certificate/certificate.service';

export const LIST = 'list';

@Injectable({
  providedIn: 'root'
})
export class CartCacheService {

  constructor(private certificateService: CertificateService) {
  }

  setCartCertificates(certificates: Array<Certificate>): void {
    localStorage.removeItem(LIST);
    localStorage.setItem(LIST, JSON.stringify(certificates));
  }

  getCartCertificates(): Array<Certificate> {
    const list = localStorage.getItem(LIST);
    if (list !== null && list !== undefined) {
      return JSON.parse(list);
    }
    return new Array<Certificate>();
  }

  addCertificate(certificate: Certificate): void {
    let certificates = this.getCartCertificates();
    if (certificates === null || certificates === undefined) {
      certificates = new Array<Certificate>();
    }
    if (certificates.length > 1000) {
      this.removeHalf();
    }
    let index = null;
    for (let i = 0; i < certificates.length; i++) {
      if (certificate.id === certificates[i].id) {
        index = i;
      }
    }
    if (index !== null) {
      certificates.splice(index, 1);
    }
    certificates.push(certificate);
    this.setCartCertificates(certificates);
  }

  removeCertificateById(id: number): void {
    const certificates = this.getCartCertificates();
    let arrayIndex = null;
    for (let i = 0; i < certificates.length; i++) {
      if (id === certificates[i].id) {
        arrayIndex = i;
      }
    }
    if (arrayIndex !== null) {
      certificates.splice(arrayIndex, 1);
    }
    this.setCartCertificates(certificates);
  }

  getCertificateById(id: number): Certificate {
    const certificates = this.getCartCertificates();
    for (const certificate of certificates) {
      if (certificate.id === id) {
        return certificate;
      }
    }
  }

  private removeHalf(): void {
    let certificates = this.getCartCertificates();
    certificates = certificates.splice(0, 500);
    this.setCartCertificates(certificates);
    console.log('cart cache service remove half:', certificates);
  }
}
