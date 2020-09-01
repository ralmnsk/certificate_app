export class UpdateUser {
  surname: string;
  name: string;
  password: string;

  constructor(surname: string, name: string, password: string) {
    this.surname = surname;
    this.name = name;
    this.password = password;
  }
}
