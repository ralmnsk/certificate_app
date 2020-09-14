import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataModalService {
  private messageSource = new BehaviorSubject<string>('');
  currentMessage = this.messageSource.asObservable();

  private backMessageSource = new BehaviorSubject<string>('');
  backMessage = this.backMessageSource.asObservable();

  constructor() {
  }

  changeMessage(message: string): void {
    this.messageSource.next(message);
  }

  changeBackMessage(backMessage: string): void {
    this.backMessageSource.next(backMessage);
  }
}
