import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { TodoAddModalComponemt } from '@app/components/modal';

@Component({
  selector: 'page-main',
  templateUrl: './page-main.html',
  styleUrls: ['./page-main.scss']
})
export class PageMainComponent implements OnInit {
  constructor(
    public dialog: MatDialog
  ) {}

  ngOnInit() {
  }

  /**
   * TODOの新規追加ダイアログ表示
   */
  openAddModal() {
    const dialogRef = this.dialog.open(TodoAddModalComponemt, {
      width: '400px',
      data: { title: "TODOの新規追加" }
    })

    dialogRef.afterClosed().subscribe(_ => {
      console.log('todo add modal is closed');
    })
  }
}
