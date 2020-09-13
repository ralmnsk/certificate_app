import {Component, OnInit} from '@angular/core';
import {DataOrderService} from '../data/data-order.service';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  isVisible = false;

  constructor(private dataOrderService: DataOrderService) {
  }

  ngOnInit(): void {
    // const modal = document.getElementById('myModal');
    // modal.style.display = 'none';
    this.dataOrderService.currentMessage
      .subscribe(data => {
        if (data === true) {
          this.showModal();
        } else if (data === false) {
          this.hide();
        }
      });
  }

  no(): void {
    this.isVisible = false;
    this.hide();
    this.dataOrderService.changeBackMessage(false);
  }

  yes(): void {
    this.isVisible = true;
    this.hide();
    this.dataOrderService.changeBackMessage(true);
  }

  private showModal(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'block';
    console.log('modal component showModal()');
  }

  hide(): void {
    const modal = document.getElementById('myModal');
    modal.style.display = 'none';
  }
}
