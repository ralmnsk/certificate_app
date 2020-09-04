import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from './user.service';
import {TokenStorageService} from '../auth/token-storage.service';
import {UpdateUser} from './update.user';
import {User} from './user';
import {DataTokenService} from '../data/data-token.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userForm: FormGroup;
  userMessage: string;
  email: string;
  surname: string;
  name: string;
  password: string;
  role: string;
  private updateUser: UpdateUser;
  private pattern: RegExp;
  private user: User;

  constructor(private router: Router,
              private userService: UserService,
              private tokenStorage: TokenStorageService,
              private dataTokenService: DataTokenService
  ) {
  }


  ngOnInit(): void {
    this.initUserForm();
    this.userService.getLoggedIn.subscribe(message => {
      this.initUserForm();
    });
  }

  initUserForm(): void {
    this.userMessage = '';
    this.userForm = new FormGroup({
      email: new FormControl('', Validators.compose(
        [
          Validators.minLength(2),
          Validators.maxLength(20),
          Validators.required
        ])),
      surname: new FormControl('', Validators.compose(
        [
          Validators.minLength(2),
          Validators.maxLength(20),
          Validators.required
        ])),
      name: new FormControl('', Validators.compose(
        [
          Validators.minLength(2),
          Validators.maxLength(20),
          Validators.required
        ])),
      role: new FormControl('', Validators.compose(
        [
          Validators.minLength(2),
          Validators.maxLength(20),
          Validators.required
        ])),
      password: new FormControl()
    });
    this.userForm.get('email').setValue(this.tokenStorage.getEmail());
    this.userForm.get('email').disable();
    this.userForm.get('surname').setValue(this.tokenStorage.getSurname());
    this.userForm.get('name').setValue(this.tokenStorage.getName());
    this.userForm.get('role').setValue(this.tokenStorage.getRole());
  }

  toLoginPage(): void {
    this.router.navigate(['login']);
  }

  saveUser(): void {
    console.log('userComponent surname:', this.userForm.get('surname').value);
    console.log('userComponent name:', this.userForm.get('name').value);
    console.log('userComponent password:', this.userForm.get('password').value);
    if (this.userForm.get('surname').invalid) {
      this.userMessage = 'surname is invalid';
      console.log(this.userMessage);
      return;
    }
    if (this.userForm.get('name').invalid) {
      this.userMessage = 'surname is invalid';
      console.log(this.userMessage);
      return;
    }
    if (this.userForm.get('password').value === null || this.userForm.get('password').value === '' || !this.isPasswordValid(this.userForm.get('password').value)) {
      this.userMessage = 'password is invalid:' + this.userForm.get('password').value;
      console.log(this.userMessage);
      return;
    }
    this.password = this.userForm.get('password').value;
    this.surname = this.userForm.get('surname').value;
    this.name = this.userForm.get('name').value;
    this.updateUser = new UpdateUser(this.surname, this.name, this.password);
    this.userService.update(this.updateUser);
    this.initUserForm();
    this.userMessage = 'New data saved successful';
  }

  isPasswordValid(password: string): boolean {
    this.pattern = new RegExp('[a-zA-Z0-9]{4,30}');
    if (this.pattern.test(password)) {
      return true;
    }
    return false;
  }
}
