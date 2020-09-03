import {Certificate} from './certificate';

export class Pagination {
  private page: number;
  private size: number;
  private tagName: string;
  private certificateName: string;
  private sort: string;
  private last: number;
  private certificates: Array<Certificate>;

  constructor() {
    this.certificates = new Array<Certificate>();
  }

  getPage(): number {
    return this.page;
  }

  setPage(value: number): void {
    this.page = value;
  }

  getSize(): number {
    return this.size;
  }

  setSize(value: number): void {
    this.size = value;
  }

  getTagName(): string {
    return this.tagName;
  }

  setTagName(value: string): void {
    this.tagName = value;
  }

  getCertificateName(): string {
    return this.certificateName;
  }

  setCertificateName(value: string): void {
    this.certificateName = value;
  }

  getSort(): string {
    return this.sort;
  }

  setSort(value: string): void {
    this.sort = value;
  }

  getLast(): number {
    return this.last;
  }

  setLast(value: number): void {
    this.last = value;
  }

  getCertificates(): Array<Certificate> {
    return this.certificates;
  }

  setCertificates(certificates: Array<Certificate>): void {
    this.certificates = certificates;
  }
}
