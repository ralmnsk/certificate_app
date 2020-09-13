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
  loginError: string;
  surnameError: string;
  nameError: string;
  passwordError: string;
  isProcessBar = false;

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
          Validators.minLength(2),
          Validators.maxLength(30),
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$'),
          Validators.required
        ])),
      surname: new FormControl('', Validators.compose(
        [
          Validators.pattern('[a-zA-Z]{2,30}'),
          Validators.required
        ])),
      name: new FormControl('', Validators.compose(
        [
          // Validators.minLength(2),
          // Validators.maxLength(30),
          Validators.pattern('[a-zA-Z]{2,30}'),
          Validators.required
        ])),
      password: new FormControl('', Validators.compose([
        // Validators.minLength(4),
        // Validators.maxLength(30),
        Validators.pattern('[a-zA-Z]{4,30}'),
        Validators.required
      ])),
      passwordRepeat: new FormControl('', Validators.compose([
        // Validators.minLength(4),
        // Validators.maxLength(30),
        Validators.pattern('[a-zA-Z]{4,30}'),
        Validators.required
      ]))
    });
  }

  registerProcess(): void {
    this.isProcessBar = true;
    this.initErrorMessages();
    const errors = new Array<string>();
    if (this.registerForm.get('email').invalid) {
      this.loginError = 'Email(login) has to be 2-30 letters. Example: your@mail.com . ';
      errors.push(this.loginError);
    }
    if (this.registerForm.get('surname').invalid) {
      this.surnameError = 'Surname has to be 2-30 latin letters.';
      errors.push(this.surnameError);
    }
    if (this.registerForm.get('name').invalid) {
      this.nameError = 'Name has to be 2-30 latin letters. ';
      errors.push(this.nameError);
    }
    if (this.registerForm.get('password').value !== this.registerForm.get('passwordRepeat').value) {
      this.passwordError = 'Repeated password is different. ';
      errors.push(this.passwordError);
    }
    if (this.registerForm.get('password').invalid) {
      this.passwordError = 'Password has to be 2-30 latin letters. ';
      errors.push(this.passwordError);
    }

    if (errors.length > 0) {
      // this.registerMessage = this.generalMessage(errors);
      this.isProcessBar = false;
      return;
    }
    if (this.registerForm.valid) {
      this.registerService.register(this.registerForm.value).subscribe(result => {
          console.log(result);
          this.registerMessage = 'User registered successfully.';
          this.router.navigate(['login']);
          this.newMessage();
          this.isProcessBar = false;
          this.registerMessage = null;
        }, error => {
          console.log('error: ', error);
          this.registerMessage = error.error.message;
          if (error.error.message === 'Entity save exception: User already exists exception' ||
            error.error.message === 'Argument(s) of entity not valid.') {
            this.registerMessage = 'User already exists. Login(Email) (2-30 characters). Surname and name (2-20 characters). Password (4-30 characters).';
          }
          this.isProcessBar = false;
          this.router.navigate(['register']);
        }
      );
    }
  }

  generalMessage(loginErrors: string[]): string {
    let text = '';
    for (const err of loginErrors) {
      text = text + err;
    }
    return text;
  }

  toLoginPage(): void {
    this.router.navigate(['login']);
  }

  private initErrorMessages(): void {
    this.loginError = null;
    this.surnameError = null;
    this.nameError = null;
    this.passwordError = null;
  }
}
