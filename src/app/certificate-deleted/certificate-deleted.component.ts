import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-certificate-deleted',
  templateUrl: './certificate-deleted.component.html',
  styleUrls: ['./certificate-deleted.component.css']
})
export class CertificateDeletedComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  certificates(): void {
    this.router.navigate(['certificates']);
  }

  orders(): void {
    this.router.navigate(['orders']);
  }
}
