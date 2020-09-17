import {Component, OnInit} from '@angular/core';
import {Tag} from '../tags/tag';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {debounceTime} from 'rxjs/operators';
import {TagsService} from '../tags/tags.service';
import {Certificate} from '../certificates/certificate';
import {CertificateService} from '../certificate/certificate.service';
import {CertificateStorageService} from '../data/certificate-storage.service';
import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {ListItem} from 'ng-multiselect-dropdown/multiselect.model';
import {DataTagEditService} from '../data/data-tag-edit.service';
import {DataModalService} from '../data/data-modal.service';
import {CERTIFICATE_CREATE, CREATE, FALSE} from '../modal/modal.component';

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
  tags = new Array<Tag>();
  addTag = new FormControl('');
  deleteTag = new FormControl('');
  message: string;
  certificate: Certificate;

  myForm: FormGroup;
  disabled = false;
  ShowFilter = true;
  limitSelection = false;
  tagsToSelect = new Array<Tag>();
  selectedItems = new Array<Tag>();
  dropdownSettings: IDropdownSettings = {};
  messageNameError: string;
  messageDurationError: string;
  messagePriceError: string;
  messageDescriptionError: string;
  isProcessing: boolean;

  constructor(private router: Router,
              private tagsService: TagsService,
              private certificateService: CertificateService,
              private certificateStorageService: CertificateStorageService,
              private fb: FormBuilder,
              private dataTagEditService: DataTagEditService,
              private dataModalService: DataModalService
  ) {
  }

  ngOnInit(): void {
    this.isProcessing = false;
    this.dataModalService.changeMessage(FALSE);
    this.dataModalService.backMessage
      .subscribe(
        value => {
          if (value === CREATE) {
            this.realSave();
          }
        }
      );
    this.myForm = this.fb.group({
      tagsControl: [this.selectedItems]
    });
    this.multiSelectDropDown();

    this.addTag.valueChanges
      .pipe(
        debounceTime(1000),
      )
      .subscribe(() => {
        console.log('addTag valueChanges:', this.addTag.value);
        if (!this.isContainTag(this.addTag.value)) {
          this.tagAddition(this.addTag.value);
          console.log('add tag value changes, tags:', this.tags, ' selectedItems:',
            this.selectedItems, ' tags to select:', this.tagsToSelect);

        }
      });
    this.deleteTag.valueChanges
      .subscribe(() => {
        this.removeTag(this.deleteTag.value);
      });
    this.initControlsValidation();
  }

  initControlsValidation(): void {
    this.name.validator = Validators.compose([
      Validators.pattern('[a-z A-Z.,!@#$%()0-9]{2,100}'),
      Validators.required
    ]);
    this.description.validator = Validators.compose([
      Validators.maxLength(990),
      Validators.required
    ]);
    this.price.validator = Validators.compose([
      Validators.pattern('^(\\d){0,1000000}\\.{0,2}\\d{0,2}$'),
      Validators.required
    ]);
    this.duration.validator = Validators.compose([
      Validators.pattern('[0-9]{0,100000}'),
      Validators.min(0),
      Validators.max(100000),
      Validators.required
    ]);
  }

  isInputErrors(): boolean {
    let flag = false;
    if (this.name.invalid) {
      this.messageNameError = 'Name has to be 2-100 letters.';
      flag = true;
    }
    if (this.description.invalid) {
      this.messageDescriptionError = 'Description has to be 0-990 letters:a-zA-Z0-9space.!&?#,;$.';
      flag = true;
    }
    if (this.price.invalid) {
      this.messagePriceError = 'Price has to be 0-1000000.Example: 1234.12';
      flag = true;
    }
    if (this.duration.invalid) {
      this.messageDurationError = 'Duration has to be 0-100000.';
      flag = true;
    }
    return flag;
  }

  initMessageErrors(): void {
    this.messageNameError = null;
    this.messageDescriptionError = null;
    this.messagePriceError = null;
    this.messageDurationError = null;
  }

  removeTag(name: string): void {
    console.log('remove Tag, name:', name);
    console.log('remove Tag, selected items start:', this.selectedItems);
    for (let i = 0; i < this.selectedItems.length; i++) {
      if (this.selectedItems[i].name === name) {
        this.selectedItems.splice(i, 1);
        this.message = 'Tag' + name + 'was removed';
      }
    }
    console.log('remove Tag, selected items end:', this.selectedItems);
  }

  tagAddition(value: string): void {
    if (value === '' || value === null || value === undefined) {
      return;
    }
    this.tagsService.create(value)
      .subscribe(data => {
          const tag = data as Tag;
          if (!this.isContainTag(tag)) {
            this.tagsService.create(tag.name)
              .pipe(debounceTime(1000))
              .subscribe(
                result => {
                  const savedTag = result as Tag;
                  if (!this.isContainTag(savedTag)) {
                    // this.addTag.setValue(savedTag);
                    this.selectedItems.push(savedTag);
                    this.tagsToSelect.push(savedTag);
                    this.myForm.get('tagsControl').setValue(this.selectedItems);
                    this.message = 'Tag was added.' + savedTag.name;
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
    console.log('isContainTag selected items:', this.selectedItems);
    for (let i = 0; i < this.selectedItems.length; i++) {
      if (this.selectedItems[i].name === tag.name) {
        return true;
      }
    }
    return false;
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  save(): void {
    this.initMessageErrors();
    if (this.isInputErrors()) {
      console.log('input errors ?:', this.isInputErrors());
      return;
    }
    this.dataModalService.changeMessage(CERTIFICATE_CREATE);
  }

  realSave(): void {
    this.isProcessing = true;
    this.certificate = new Certificate();
    this.certificate.name = this.name.value;
    this.certificate.description = this.description.value;
    this.certificate.duration = this.duration.value;
    this.certificate.price = this.price.value;
    this.certificateService.save(this.certificate)
      .subscribe(
        result => {
          this.certificate = result as Certificate;
          this.certificateStorageService.setCurrentCertificate(this.certificate.id);
          this.message = 'Certificate was created.';
          this.saveTagsOfCreatedCertificate();
          this.router.navigate(['certificate']);
          this.isProcessing = false;
        }, error => {
          console.log(error.error.message);
          this.message = 'Error happened during save';
          this.isProcessing = false;
        }
      );
  }

  saveTagsOfCreatedCertificate(): void {
    for (let i = 0; i < this.selectedItems.length; i++) {
      this.certificateService.addTagToCertificate(this.certificate.id, this.selectedItems[i].id)
        .pipe(debounceTime(1000))
        .subscribe(
          result => {
            this.certificate = result as Certificate;
            this.message = 'Tag was added.';
            if (i === this.selectedItems.length - 1) {
              this.dataTagEditService.changeMessage(this.certificate.id.toString());
            }
          }, error => {
            console.log(error.message);
            this.message = 'Error happened during tags saving.';
          }
        );
    }
  }

  // private isFormValid(): boolean {
  //   this.name.validator = Validators.compose([
  //     Validators.minLength(2),
  //     Validators.maxLength(256),
  //     Validators.required
  //   ]);
  //   if (this.name.invalid) {
  //     return false;
  //   }
  //   this.description.validator = Validators.compose([
  //     Validators.minLength(0),
  //     Validators.maxLength(999),
  //     Validators.required
  //   ]);
  //   if (this.description.invalid) {
  //     return false;
  //   }
  //   this.duration.validator = Validators.compose([
  //     Validators.min(0),
  //     Validators.max(10000),
  //     Validators.required
  //   ]);
  //   if (this.duration.invalid) {
  //     return false;
  //   }
  //   this.price.validator = Validators.compose([
  //     Validators.min(0),
  //     Validators.max(1000000),
  //     Validators.required
  //   ]);
  //   if (this.price.invalid) {
  //     return false;
  //   }
  //   console.log('form is valid');
  //   return true;
  // }

  onFilterTextChange($event: ListItem): void {
    const value = String($event);
    this.addTag.setValue(value);

  }

  onItemDeselect($event: ListItem): void {
    const name = String($event);
    console.log('item deselect', String($event));
    this.removeTag(name);
  }

  onItemSelect(item: any): void {
    console.log(item);
  }

  onSelectAll(items: any): void {
    console.log(items);
  }

  multiSelectDropDown(): void {
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      // selectAllText: 'Select All',
      // unSelectAllText: 'UnSelect All',
      enableCheckAll: false,
      itemsShowLimit: 10,
      allowSearchFilter: this.ShowFilter,
      allowRemoteDataSearch: true
    };
    console.log('multi select drop down:');
  }

}
