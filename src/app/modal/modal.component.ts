import {Component, OnInit} from '@angular/core';
import {DataModalService} from '../data/data-modal.service';

export const FALSE = 'false';
export const TRUE = 'true';
export const DELETE = 'delete';
export const UPDATE = 'update';
export const CREATE = 'create';
export const EMPTY = '';
export const ORDER = 'order';
export const SUBMIT = 'submit';
export const ORDER_VIEW = 'order-view';
export const ORDER_MESSAGE = 'Do you really want to clear your cart?';
export const ORDER_VIEW_MESSAGE = 'Do you really want to complete?';
export const CERTIFICATE_CREATE = 'certificate-create';
export const CERTIFICATE_CREATE_MESSAGE = 'Do you really want to create a certificate?';
export const CERTIFICATE_UPDATE = 'certificate-update';
export const CERTIFICATE_UPDATE_MESSAGE = 'Do you really want to update?';
export const CERTIFICATE_DELETE = 'certificate-delete';
export const CERTIFICATE_DELETE_MESSAGE = 'Do you really want to delete?';
export const SUBMIT_ORDER_MESSAGE = 'Do you really want to submit?';
export const SUBMIT_ORDER = 'submit-order';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  modalMessage: string;
  backMessage: string;
  constructor(private dataOrderService: DataModalService) {
  }

  ngOnInit(): void {
    this.dataOrderService.currentMessage
      .subscribe(data => {
        switch (data) {
          case ORDER: {
            this.showModal();
            this.modalMessage = ORDER_MESSAGE;
            this.backMessage = TRUE;
            break;
          }
          case ORDER_VIEW: {
            this.showModal();
            this.modalMessage = ORDER_VIEW_MESSAGE;
            this.backMessage = TRUE;
            break;
          }
          case CERTIFICATE_UPDATE: {
            this.showModal();
            this.modalMessage = CERTIFICATE_UPDATE_MESSAGE;
            this.backMessage = UPDATE;
            break;
          }
          case CERTIFICATE_CREATE: {
            this.showModal();
            this.modalMessage = CERTIFICATE_CREATE_MESSAGE;
            this.backMessage = CREATE;
            break;
          }
          case CERTIFICATE_DELETE: {
            this.showModal();
            this.modalMessage = CERTIFICATE_DELETE_MESSAGE;
            this.backMessage = DELETE;
            break;
          }
          case SUBMIT_ORDER: {
            this.showModal();
            this.modalMessage = SUBMIT_ORDER_MESSAGE;
            this.backMessage = SUBMIT;
            break;
          }
          case FALSE: {
            this.hide();
            this.modalMessage = EMPTY;
            break;
          }
        }

      });
  }

  no(): void {
    this.hide();
    this.dataOrderService.changeBackMessage('false');
  }

  yes(): void {
    this.hide();
    this.dataOrderService.changeBackMessage(this.backMessage);
  }

  private showModal(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'block';
  }

  hide(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'none';
  }
}
