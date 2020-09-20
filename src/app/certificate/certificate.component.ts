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
import {DataModalService} from '../data/data-modal.service';
import {DELETE, FALSE, UPDATE} from '../modal/modal.component';
import {TokenStorageService} from '../auth/token-storage.service';
import {CartCacheService} from '../cache/cart-cache.service';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.css']
})
export class CertificateComponent implements OnInit {

  id: number;
  certificate: Certificate;


  tags: Array<Tag>;
  // addTag = new FormControl('');

  message: string;
  messageCrudOperations: string;
  title = 'CodeSandbox';
  myForm: FormGroup;
  disabled = false;
  ShowFilter = true;
  limitSelection = false;
  tagsToSelect: Array<any> = [];
  selectedItems: Array<any> = [];
  dropdownSettings: IDropdownSettings = {};

  addTag = new FormControl();
  userRole: string;
  isProcessing: boolean;
  nameControl: FormControl;
  description: FormControl;
  creation: FormControl;
  modification: FormControl;
  duration: FormControl;
  price: FormControl;

  constructor(private dataTagEditService: DataTagEditService,
              private certificateService: CertificateService,
              private router: Router,
              private tagsService: TagsService,
              private certificateStorageService: CertificateStorageService,
              private fb: FormBuilder,
              private dataModal: DataModalService,
              private tokenStorage: TokenStorageService,
              private cartCacheService: CartCacheService
  ) {
  }

  ngOnInit(): void {
    this.certificate = new Certificate();
    this.nameControl = new FormControl('');
    this.description = new FormControl('');
    this.creation = new FormControl('');
    this.modification = new FormControl('');
    this.duration = new FormControl('');
    this.price = new FormControl('');
    this.myForm = this.fb.group({
      tagsControl: [this.selectedItems]
    });
    this.isProcessing = false;
    this.userRole = this.tokenStorage.getRole();
    this.initialGetCertificate();
    this.dataModal.changeMessage(FALSE);
    this.dataModal.backMessage
      .subscribe(data => {
        if (data === UPDATE) {
          this.update();
          console.log('certificate, received message to update');
        }
        if (data === DELETE) {
          this.delete();
        }
      });

    this.dataTagEditService.currentMessage.subscribe(message => {
      this.id = Number(message);
      this.initialGetCertificate();
    });
    // if (this.id === 0 || this.id === undefined) {
    //   this.id = this.certificateStorageService.getCurrentCertificateId();

    // }
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
      // selectAllText: 'Select All',
      // unSelectAllText: 'UnSelect All',
      enableCheckAll: false,
      itemsShowLimit: 10,
      allowSearchFilter: this.ShowFilter,
      allowRemoteDataSearch: true
    };
    // console.log('this.tags:', this.tags);
  }

  onItemSelect(item: any): void {
    // console.log(item);
  }

  onSelectAll(items: any): void {
    // console.log(items);
  }


  initialGetCertificate(): void {
    this.isProcessing = true;
    this.id = this.certificateStorageService.getCurrentCertificateId();
    this.certificateService.getCertificate(this.id).subscribe(
      data => {
        this.certificate = data as Certificate;
        this.cartCacheService.addCertificate(this.certificate);
        this.fillValues();
        this.loadTags();
        this.isProcessing = false;
        console.log('4certificate, initialGetCertificate: certificate:', this.certificate);
      },
      error => {
        console.log('certificate, initialGetCertificate, error happened during getting certificate');
        this.isProcessing = false;
      }
    );
    // }
    console.log('certificate initialGetCertificate, this.certificate:', this.certificate);
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
    this.isProcessing = false;
  }

  loadTags(): void {
    this.tagsService.getTagsByCertificateId(this.certificate.id)
      .subscribe(data => {
          this.tags = data.elements.content as Array<Tag>;
          this.multiSelectDropDown();
          this.isProcessing = false;
          // console.log('certificate component, load tags:', this.tags);
        }, error => {
          console.log(error.message);
          this.isProcessing = false;
        }
      );
  }

  back(): void {
    this.router.navigate(['certificates']);
  }

  preUpdate(): void {
    this.dataModal.changeMessage('certificate-update');
  }

  update(): void {
    this.isProcessing = true;
    if (!this.isSaveEnabled()) {
      // console.log('isDisabled');
      return;
    }
    this.disableSave();
    this.certificate.name = this.nameControl.value;
    this.certificate.description = this.description.value;
    this.certificate.duration = this.duration.value;
    this.certificate.price = this.price.value;

    this.certificateService.update(this.certificate)
      .subscribe(data => {
          this.certificate = data as Certificate;
          this.isProcessing = false;
          this.fillValues();
          this.loadTags();
          this.cartCacheService.addCertificate(this.certificate);
          this.messageCrudOperations = 'Certificate data was updated.';
          console.log('certificate data was updated');
          this.isProcessing = false;
          this.enableSave();
        }, error => {
          this.isProcessing = false;
          console.log(error.message, error.error.message);
          this.message = error.error.message;
          if (this.message !== null && this.message !== undefined) {
            this.message = 'Certificate was included in some orders, so it could not be saved or deleted.';
          }
          this.enableSave();
        }
      );
    this.isProcessing = false;
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

  preDelete(): void {
    this.dataModal.changeMessage('certificate-delete');
  }

  delete(): void {
    if (!this.isDeleteEnabled()) {
      // console.log('isDisabled');
      return;
    }
    this.isProcessing = true;
    this.disableDelete();
    this.certificateService.delete(this.certificate.id)
      .subscribe(data => {
          this.message = 'Certificate data was deleted.';
          this.cartCacheService.removeCertificateById(this.certificate.id);
          this.certificate = null;
          this.router.navigate(['certificate-deleted']);
          this.enableDelete();
          this.isProcessing = false;
        }, error => {
          this.isProcessing = false;
          console.log(error.error.message);
          if (error.error.message !== null && error.error.message !== undefined) {
            const errorMessage = error.error.message;
            const maybeMessage = 'certificate was included';
            if (errorMessage.indexOf(maybeMessage) >= 0) {
              this.message = 'Certificate delete: certificate was included in some orders. It can not be deleted.';
            }
          }
          this.enableDelete();
          this.isProcessing = false;
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
    // console.log('tag addition', this.addTag.value);
    if (value === '' || value === null || value === undefined) {
      return;
    }
    this.tagsService.create(value)
      .subscribe(data => {
          const tag = data as Tag;
          // console.log('tag created(got) from db:', tag);
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
    // if (this.tags === null || this.tags === undefined) {
    //   this.tags = new Array<Tag>();
    //   return false;
    // }
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
    // console.log('item deselect', id);
    this.removeTag(Number(id));
  }
}
