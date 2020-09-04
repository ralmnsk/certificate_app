import {Component, OnInit} from '@angular/core';
import {Tag} from '../tags/tag';
import {FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {debounceTime} from 'rxjs/operators';
import {TagsService} from '../tags/tags.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css']
})
export class CreateCertificateComponent implements OnInit {
  name = new FormControl('');
  description = new FormControl('');
  duration = new FormControl('');
  price = new FormControl('');
  tags: Array<Tag>;
  addTag = new FormControl('');
  message: string;


  constructor(private router: Router,
              private tagsService: TagsService
  ) {
  }

  ngOnInit(): void {
    this.tags = new Array<Tag>();
  }

  removeTag(id: number): void {
    for (let i = 0; i < this.tags.length; i++) {
      if (this.tags[i].id === id) {
        this.tags.splice(i, 1);
        console.log('remove Tag, tags:', this.tags);
      }
    }
  }

  tagAddition(): void {
    if (this.addTag.value === '' || this.addTag.value === null || this.addTag.value === undefined) {
      return;
    }
    this.tagsService.create(this.addTag.value)
      .subscribe(data => {
          const tag = data as Tag;
          if (!this.isContainTag(tag)) {
            this.tagsService.create(tag.name)
              .pipe(debounceTime(1000))
              .subscribe(
                result => {
                  const savedTag = result as Tag;
                  if (!this.isContainTag(savedTag)) {
                    this.tags.push(savedTag);
                    this.message = 'Tag was added.';
                  }
                }, error => {
                  console.log(error.message);
                }
              );
          }
        }, error => {
          console.log(error.message);
        }
      );
  }

  isContainTag(tag: Tag): boolean {
    for (let i = 0; i < this.tags.length; i++) {
      if (this.tags[i].name === tag.name) {
        return true;
      }
    }
    return false;
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  save(): void {
    this.isFormValid();

  }

  private isFormValid(): boolean {
    this.name.validator = Validators.compose([
      Validators.minLength(2),
      Validators.maxLength(30),
      Validators.required
    ]);
    if (this.name.invalid) {
      return false;
    }
    this.description.validator = Validators.compose([
      Validators.minLength(0),
      Validators.maxLength(999),
      Validators.required
    ]);
    if (this.description.invalid) {
      return false;
    }
    this.duration.validator = Validators.compose([
      Validators.min(0),
      Validators.max(10000),
      Validators.required
    ]);
    if (this.duration.invalid) {
      return false;
    }
    this.price.validator = Validators.compose([
      Validators.min(0),
      Validators.max(1000000),
      Validators.required
    ]);
    if (this.price.invalid) {
      return false;
    }
    console.log('form is valid');
    return true;
  }
}
