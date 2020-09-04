import {Injectable} from '@angular/core';
import {Pagination} from '../certificates/pagination';

@Injectable({
  providedIn: 'root'
})
export class CertificateStorageService {


  private pagination: Pagination;

  constructor() {
    this.pagination = new Pagination();
  }

  getPagination(): Pagination {
    return this.pagination;
  }

  setPagination(value: Pagination): void {
    this.pagination = value;
  }

  getCurrentCertificateId(): number {
    return Number(window.sessionStorage.getItem('CertificateId'));
  }

  setCurrentCertificate(value: number): void {
    window.sessionStorage.removeItem('CertificateId');
    window.sessionStorage.setItem('CertificateId', value.toString());
  }
}
