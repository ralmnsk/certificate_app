export class Tag {
  id: number;
  name: string;

  // constructor(id: number, name: string) {
  //   this.id = id;
  //   this.name = name;
  // }

  constructor() {
  }

  getId(): number {
    return this.id;
  }

  setId(value: number): void {
    this.id = value;
  }

  getName(): string {
    return this.name;
  }

  setName(value: string): void {
    this.name = value;
  }
}
