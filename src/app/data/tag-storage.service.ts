import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TagStorageService {


  private tagName: string;

  constructor() {
    this.tagName = '';
  }

  setTagName(value: string): void {
    this.tagName = value;
  }

  getTagName(): string {
    return this.tagName;
  }
}
