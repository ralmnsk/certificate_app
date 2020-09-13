import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataOrderService {
  private messageOrderSource = new BehaviorSubject<boolean>(false);
  currentMessage = this.messageOrderSource.asObservable();

  private backMessageSource = new BehaviorSubject<boolean>(false);
  backMessage = this.backMessageSource.asObservable();

  constructor() {
  }

  changeMessage(message: boolean): void {
    this.messageOrderSource.next(message);
  }

  changeBackMessage(backMessage: boolean): void {
    this.backMessageSource.next(backMessage);
  }
}
