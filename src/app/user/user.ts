export class User {
  id: number;
  surname: string;
  name: string;
  email: string;
  role: string;

  constructor(id: number, surname: string, name: string, email: string, role: string) {
    this.id = id;
    this.surname = surname;
    this.name = name;
    this.email = email;
    this.role = role;
  }
}
