import { Component, OnInit, Input } from '@angular/core';
import { Todo } from '@shared/model'

@Component({
  selector: 'ui-menu',
  templateUrl: './ui-menu.html',
  styleUrls: ['./ui-menu.scss']
})
export class UiMenuComponent implements OnInit {
  @Input('rows') todos: Todo[]

  constructor() { }

  ngOnInit() {
  }

}
