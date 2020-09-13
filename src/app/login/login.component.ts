import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from '../auth/token-storage.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth/auth.service';
import {UserService} from '../user/user.service';
import {DataService} from '../data/data.service';
import {config} from '../config';
import {User} from '../user/user';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  private userLink: string;
  messageLogin: string;
  private newUrl: string;
  private user: User;
  hide = true;
  loginError: string;
  passwordError: string;
  isProcessBar = false;

  constructor(private router: Router,
              private authService: AuthService,
              private userService: UserService,
              private tokenStorage: TokenStorageService,
              private dataService: DataService
  ) {
  }


  ngOnInit(): void {
    this.initForm();
    this.messageLogin = '';
    this.dataService.currentMessage.subscribe(message => this.messageLogin = message);
  }

  initForm(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', Validators.compose(
        [
          Validators.minLength(2),
          Validators.maxLength(30),
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$'),
          Validators.required
        ])),
      password: new FormControl('', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(30),
        Validators.pattern('[a-zA-z]{4,30}'),
        Validators.required
      ]))
    })
    ;
  }

  loginProcess(): void {
    this.isProcessBar = true;
    this.messageLogin = null;
    this.tokenStorage.logout();
    this.initErrorMessages();
    if (this.loginForm.invalid) {
      if (this.loginForm.get('email').invalid) {
        this.loginError = 'Login has to be 2- 30 latin letters';
      }
      if (this.loginForm.get('password').invalid) {
        this.passwordError = 'Password has to be 4-30 latin letters';
      }
      console.log('login or password is invalid');
      this.isProcessBar = false;
      return;
    }
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe(result => {
          this.tokenStorage.setToken(result.token);
          this.userLink = result.user_link.replace(config.ServerUrl, config.Url);
          console.log('userLink:', this.userLink);
          this.userService.setUserInStorage(this.userLink);
          this.router.navigate(['certificates']);
          this.isProcessBar = false;
        }, error => {
          console.log('error: ', error.error.message);
          this.messageLogin = 'Login or password is incorrect';
          this.router.navigate(['login']);
          this.isProcessBar = false;
        }
      );
    }
  }

  toRegisterPage(): void {
    this.router.navigate(['register']);
  }

  private initErrorMessages(): void {
    this.loginError = null;
    this.passwordError = null;
  }
}
