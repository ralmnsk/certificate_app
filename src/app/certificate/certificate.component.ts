import {Component, OnInit} from '@angular/core';
import {DataTagEditService} from '../data/data-tag-edit.service';
import {CertificateService} from './certificate.service';
import {Certificate} from '../certificates/certificate';
import {FormControl} from '@angular/forms';
import {Router} from '@angular/router';
import {TagsService} from '../tags/tags.service';
import {Tag} from '../tags/tag';
import {debounceTime} from 'rxjs/operators';
import {CertificateStorageService} from '../data/certificate-storage.service';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.css']
})
export class CertificateComponent implements OnInit {
  id: number;
  certificate: Certificate;

  name = new FormControl('');
  description = new FormControl('');
  file = new FormControl('');
  creation = new FormControl('');
  modification = new FormControl('');
  duration = new FormControl('');
  price = new FormControl('');

  tags: Array<Tag>;
  addTag = new FormControl('');

  message: string;

  constructor(private dataTagEditService: DataTagEditService,
              private certificateService: CertificateService,
              private router: Router,
              private tagsService: TagsService,
              private certificateStorageService: CertificateStorageService
  ) {
  }

  ngOnInit(): void {
    this.dataTagEditService.currentMessage.subscribe(message => {
      this.id = Number(message);
      this.initialGetCertificate();
    });
    if (this.id === 0 || this.id === undefined) {
      this.id = this.certificateStorageService.getCurrentCertificateId();
      this.initialGetCertificate();
    }
    // console.log('certificate initialization, certificate id:' + this.id);
  }

  initialGetCertificate(): void {
    this.certificateService.getCertificate(this.id)
      .subscribe(data => {
          this.certificate = data as Certificate;
          // console.log('certificate:', this.certificate);
          this.fillValues();
          this.loadTags();
        }, error => {
          console.log(error.message);
        }
      );
  }

  fillValues(): void {
    this.name.setValue(this.certificate.name);
    this.description.setValue(this.certificate.description);
    this.creation.setValue(this.certificate.creation);
    this.modification.setValue(this.certificate.modification);
    this.duration.setValue(this.certificate.duration);
    this.price.setValue(this.certificate.price);
  }

  loadTags(): void {
    this.tagsService.getTagsByCertificateId(this.certificate.id)
      .subscribe(data => {
          this.tags = data.elements.content as Array<Tag>;
          // console.log('tags:', this.tags);
        }, error => {
          console.log(error.message);
        }
      );
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  save(): void {
    if (!this.isSaveEnabled()) {
      // console.log('isDisabled');
      return;
    }
    this.disableSave();
    // console.log('disable save');
    this.certificate.name = this.name.value;
    this.certificate.description = this.description.value;
    this.certificate.duration = this.duration.value;
    this.certificate.price = this.price.value;

    this.certificateService.update(this.certificate)
    // .pipe(debounce<string>(async () => console.log('timeout:', Date.now())))
    // .pipe(debounceTime(2000))
      .subscribe(data => {
          this.certificate = data as Certificate;
          this.fillValues();
          this.loadTags();
          this.message = 'Certificate data was updated.';
          this.enableSave();
          // console.log('enable save');
        }, error => {
          console.log(error.message);
          this.enableSave();
          // console.log('enable save');
        }
      );
  }

  disableSave(): void {
    let btn = document.getElementById('save');
    if (btn !== null && btn !== undefined) {
      btn.id = 'save-disabled';
    }
  }

  enableSave(): void {
    let btn = document.getElementById('save-disabled');
    if (btn !== null && btn !== undefined) {
      btn.id = 'save';
    }
  }

  isSaveEnabled(): boolean {
    let btn = document.getElementById('save');
    if (btn !== null && btn !== undefined) {
      return true;
    }
    return false;
  }

  delete(): void {
    if (!this.isDeleteEnabled()) {
      console.log('isDisabled');
      return;
    }
    this.disableDelete();
    this.certificateService.delete(this.certificate.id)
      .subscribe(data => {
          this.message = 'Certificate data was deleted.';
          this.certificate = null;
          this.router.navigate(['certificate-deleted']);
          this.enableDelete();
        }, error => {
          console.log(error.message);
          this.enableDelete();
        }
      );
  }

  disableDelete(): void {
    let btn = document.getElementById('delete');
    if (btn !== null && btn !== undefined) {
      btn.id = 'delete-disabled';
    }
  }

  enableDelete(): void {
    let btn = document.getElementById('delete-disabled');
    if (btn !== null && btn !== undefined) {
      btn.id = 'delete';
    }
  }

  isDeleteEnabled(): boolean {
    let btn = document.getElementById('delete');
    if (btn !== null && btn !== undefined) {
      return true;
    }
    return false;
  }

  tagAddition(): void {
    if (this.addTag.value === '' || this.addTag.value === null || this.addTag.value === undefined) {
      return;
    }
    this.tagsService.create(this.addTag.value)
      .subscribe(data => {
          const tag = data as Tag;
          if (!this.isContainTag(tag)) {
            this.certificateService.addTagToCertificate(this.certificate.id, tag.id)
              .pipe(debounceTime(1000))
              .subscribe(
                result => {
                  this.certificate = result as Certificate;
                  if (!this.isContainTag(tag)) {
                    this.tags.push(tag);
                    this.message = 'Tag was added.';
                  }
                }, error => {
                  console.log(error.message);
                }
              );
          }
          // console.log('tag:', tag);
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

  removeTag(tagId: number): void {
    // console.log('remove tag');
    this.tagsService.remove(this.certificate.id, tagId)
      .subscribe((data) => {
          this.initialGetCertificate();
        }, error => {
          console.log(error.message);
        }
      );
  }
}
