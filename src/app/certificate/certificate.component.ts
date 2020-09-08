import {Component, OnInit} from '@angular/core';
import {DataTagEditService} from '../data/data-tag-edit.service';
import {CertificateService} from './certificate.service';
import {Certificate} from '../certificates/certificate';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {TagsService} from '../tags/tags.service';
import {Tag} from '../tags/tag';
import {debounceTime} from 'rxjs/operators';
import {CertificateStorageService} from '../data/certificate-storage.service';
import {IDropdownSettings, ListItem} from 'ng-multiselect-dropdown/multiselect.model';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.css']
})
export class CertificateComponent implements OnInit {

  id: number;
  certificate: Certificate;

  nameControl = new FormControl('');
  description = new FormControl('');
  file = new FormControl('');
  creation = new FormControl('');
  modification = new FormControl('');
  duration = new FormControl('');
  price = new FormControl('');

  tags: Array<Tag>;
  // addTag = new FormControl('');

  message: string;

  title = 'CodeSandbox';
  myForm: FormGroup;
  disabled = false;
  ShowFilter = true;
  limitSelection = false;
  tagsToSelect: Array<any> = [];
  selectedItems: Array<any> = [];
  dropdownSettings: IDropdownSettings = {};

  addTag = new FormControl();

  constructor(private dataTagEditService: DataTagEditService,
              private certificateService: CertificateService,
              private router: Router,
              private tagsService: TagsService,
              private certificateStorageService: CertificateStorageService,
              private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.myForm = this.fb.group({
      tagsControl: [this.selectedItems]
    });
    this.dataTagEditService.currentMessage.subscribe(message => {
      this.id = Number(message);
      this.initialGetCertificate();
    });
    if (this.id === 0 || this.id === undefined) {
      this.id = this.certificateStorageService.getCurrentCertificateId();
      this.initialGetCertificate();
    }
    this.addTag.valueChanges
      .pipe(
        debounceTime(1000),
      )
      .subscribe(() => {
        this.tagAddition(this.addTag.value);
      });
  }

  multiSelectDropDown(): void {
    // console.log('this.tags:', this.tags);
    this.tagsToSelect = this.tags;
    this.selectedItems = this.tags;
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 10,
      allowSearchFilter: this.ShowFilter
    };
    // console.log('this.tags:', this.tags);
  }

  onItemSelect(item: any): void {
    console.log(item);
  }

  onSelectAll(items: any): void {
    console.log(items);
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
          // this.message = 'Error happened during certificate getting';
        }
      );
  }

  fillValues(): void {
    this.nameControl.setValue(this.certificate.name);
    this.description.setValue(this.certificate.description);
    this.creation.setValue(this.certificate.creation.toString().replace('T', ' '));
    if (this.certificate.modification !== null && this.certificate.modification !== undefined) {
      this.modification.setValue(this.certificate.modification.toString().replace('T', ' '));
    } else {
      this.modification.setValue('---');
    }
    this.duration.setValue(this.certificate.duration);
    this.price.setValue(this.certificate.price);
  }

  loadTags(): void {
    this.tagsService.getTagsByCertificateId(this.certificate.id)
      .subscribe(data => {
          this.tags = data.elements.content as Array<Tag>;
          this.multiSelectDropDown();
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
    this.certificate.name = this.nameControl.value;
    this.certificate.description = this.description.value;
    this.certificate.duration = this.duration.value;
    this.certificate.price = this.price.value;
    // this.certificate.creation = null;
    // this.modification = null;
    // console.log('this certificate', this.certificate);

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
          this.message = error.error.message;
          this.enableSave();
          // console.log('enable save');
        }
      );
  }

  disableSave(): void {
    const btn = document.getElementById('save');
    if (btn !== null && btn !== undefined) {
      btn.id = 'save-disabled';
    }
  }

  enableSave(): void {
    const btn = document.getElementById('save-disabled');
    if (btn !== null && btn !== undefined) {
      btn.id = 'save';
    }
  }

  isSaveEnabled(): boolean {
    const btn = document.getElementById('save');
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
    const btn = document.getElementById('delete');
    if (btn !== null && btn !== undefined) {
      btn.id = 'delete-disabled';
    }
  }

  enableDelete(): void {
    const btn = document.getElementById('delete-disabled');
    if (btn !== null && btn !== undefined) {
      btn.id = 'delete';
    }
  }

  isDeleteEnabled(): boolean {
    const btn = document.getElementById('delete');
    if (btn !== null && btn !== undefined) {
      return true;
    }
    return false;
  }

  tagAddition(value: string): void {
    console.log('tag addition', this.addTag.value);
    if (value === '' || value === null || value === undefined) {
      return;
    }
    this.tagsService.create(value)
      .subscribe(data => {
          const tag = data as Tag;
          console.log('tag created(got) from db:', tag);
          if (!this.isContainTag(tag)) {
            this.certificateService.addTagToCertificate(this.certificate.id, tag.id)
              .pipe(debounceTime(1000))
              .subscribe(
                result => {
                  this.certificate = result as Certificate;
                  if (!this.isContainTag(tag)) {
                    this.tags.push(tag);
                    this.loadTags();
                    this.message = 'Tag was added.';
                  }
                }, error => {
                  console.log(error.message);
                  this.message = 'Error happened during tags saving.';
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
          this.loadTags();
        }, error => {
          console.log(error.message);
        }
      );
  }

  onFilterTextChange($event: ListItem): void {
    const value = String($event);
    this.addTag.setValue(value);
  }

  onItemDeselect($event: ListItem): void {
    const id = $event.id;
    console.log('item deselect', id);
    this.removeTag(Number(id));
  }
}
