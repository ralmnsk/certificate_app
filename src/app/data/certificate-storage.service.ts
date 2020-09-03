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
}
