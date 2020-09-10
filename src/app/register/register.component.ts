import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegisterService} from './register.service';
import {DataService} from '../data/data.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  registerMessage: string;

  constructor(private router: Router,
              private registerService: RegisterService,
              private dataService: DataService
  ) {
  }

  ngOnInit(): void {
    this.initForm();
    this.registerMessage = '';
    this.dataService.currentMessage.subscribe(message => this.registerMessage = message);
  }

  newMessage(): void {
    this.dataService.changeMessage(this.registerMessage);
  }

  initForm(): void {
    this.registerForm = new FormGroup({
      email: new FormControl('', Validators.compose(
        [
          Validators.min(2),
          Validators.max(30),
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$'),
          Validators.required
        ])),
      surname: new FormControl('', Validators.compose(
        [
          Validators.min(2),
          Validators.max(30),
          Validators.required
        ])),
      name: new FormControl('', Validators.compose(
        [
          Validators.min(2),
          Validators.max(30),
          Validators.required
        ])),
      password: new FormControl('', Validators.compose([
        Validators.min(4),
        Validators.max(30),
        Validators.required
      ])),
      passwordRepeat: new FormControl('', Validators.compose([
        Validators.min(4),
        Validators.max(30),
        Validators.required
      ]))
    });
  }

  registerProcess(): void {
    if (this.registerForm.get('password').value !== this.registerForm.get('passwordRepeat').value) {
      this.registerMessage = 'Repeated password is different';
      console.log('repeated password is different');
      return;
    }
    if (this.registerForm.invalid) {
      console.log('Registration data is invalid');
      this.registerMessage = 'Registration data is invalid';
      return;
    }
    if (this.registerForm.valid) {
      this.registerService.register(this.registerForm.value).subscribe(result => {
          console.log(result);
          this.registerMessage = 'User registered successful.';
          this.router.navigate(['login']);
          this.newMessage();
        }, error => {
          console.log('error: ', error);
          this.registerMessage = error.error.message;
          if (error.error.message === 'Entity save exception: User already exists exception') {
            this.registerMessage = 'User already exists. Login(Email) (2-30 characters). Surname and name (2-20 characters). Password (4-30 characters).';
          }
          this.router.navigate(['register']);
        }
      );
    }
  }

  toLoginPage(): void {
    this.router.navigate(['login']);
  }

}
