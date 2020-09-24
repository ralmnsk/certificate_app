import {AfterViewChecked, Component, OnInit} from '@angular/core';
import {TagsPagination} from './tags-pagination';
import {TagsService} from './tags.service';
import {Tag} from './tag';
import {DataTagService} from '../data/data-tag.service';

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.css']
})
export class TagsComponent implements OnInit, AfterViewChecked {
  private pagination: TagsPagination;
  tags: Array<Tag>;

  constructor(private tagsService: TagsService,
              private dataTagService: DataTagService
  ) {
    this.pagination = new TagsPagination();
    this.pagination.setName('');
    this.pagination.setPage(0);
    this.pagination.setSize(10);
    this.pagination.setSort('name+');
  }

  ngOnInit(): void {
    this.receiveTags();
  }

  ngAfterViewChecked(): void {
    if (this.tags !== null && this.tags !== undefined) {
      for (const tag of this.tags) {
        this.randomBgColor(tag.id);
      }
    }
  }

  receiveTags(): void {
    this.disableButtons();
    this.tagsService.getTags(this.pagination.getPage(), this.pagination.getSize(),
      this.pagination.getName(), this.pagination.getSort())
      .subscribe(
        data => {
          this.pagination.setTags(data.elements.content as Array<Tag>);
          this.tags = data.elements.content as Array<Tag>;
          this.pagination.setLast(data.totalPage);
        }, error => {
          console.log(error.message);
        }
      );
    this.enableButtons();
  }

  previous(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.pagination.setPage(this.pagination.getPage() - 1);
    if (this.pagination.getPage() < 0) {
      this.pagination.setPage(0);
    }
    this.receiveTags();
  }

  next(): void {
    if (this.isButtonsDisabled()) {
      return;
    }
    this.pagination.setPage(this.pagination.getPage() + 1);
    if (this.pagination.getPage() > this.pagination.getLast()) {
      this.pagination.setPage(this.pagination.getLast());
    }
    this.receiveTags();
  }

  disableButtons(): void {
    document.getElementById('left').id = 'left_disabled';
    document.getElementById('right').id = 'right_disabled';
  }

  enableButtons(): void {
    document.getElementById('left_disabled').id = 'left';
    document.getElementById('right_disabled').id = 'right';
  }

  isButtonsDisabled(): boolean {
    if (document.getElementById('left_disabled') === null) {
      return false;
    }
    return true;
  }

  setTagName(name: string): void {
    this.dataTagService.changeMessage(name);
  }

  randomBgColor(id: number): void {
    const x = Math.floor(Math.random() * 256);
    const y = Math.floor(Math.random() * 256);
    const z = Math.floor(Math.random() * 256);
    const rgb = 'rgb(' + x + ',' + y + ',' + z + ')';
    const tagElement = document.getElementById(id.toString());
    if (tagElement !== null && tagElement !== undefined) {
      tagElement.style.color = rgb;
      tagElement.style.backgroundColor = 'white';
    }
  }
}
