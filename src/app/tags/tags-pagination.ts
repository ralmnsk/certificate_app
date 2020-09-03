import {Tag} from './tag';
import {Certificate} from '../certificates/certificate';

export class TagsPagination{
  private page: number;
  private size: number;
  private name: string;
  private sort: string;
  private last: number;
  private tags: Array<Tag>;

  constructor() {
    this.tags = new Array<Tag>();
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

  getName(): string {
    return this.name;
  }

  setName(value: string): void {
    this.name = value;
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

  getTags(): Array<Tag> {
    return this.tags;
  }

  setTags(tags: Array<Tag>): void {
    this.tags = tags;
  }
}
