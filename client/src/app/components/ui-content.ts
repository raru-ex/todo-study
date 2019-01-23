import {Component, OnInit, Input } from '@angular/core';
import { Todo } from '@shared/model'

@Component({
  selector: 'ui-content',
  templateUrl: './ui-content.html',
  styleUrls: ['./ui-content.scss']
})
export class UiContentComponent implements OnInit {
  @Input() todo: Todo

  constructor() { }

  ngOnInit() {
  }
}
