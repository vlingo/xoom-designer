import { Component, OnInit, Input, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, AfterViewInit  {
  
  private $originalRow: JQuery<HTMLElement>;
  
  @Input() resourceName: String;
  @ViewChild('table', { read: ElementRef }) table: ElementRef;
  
  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    $(this.table.nativeElement).on('click', '.remove-button', this.removeRow);
    this.$originalRow = $(this.table.nativeElement).find('.row').last();
    $(this.table.nativeElement).find('.row').detach();
  }

  addRow() {
    $(this.table.nativeElement).append(this.$originalRow.clone(true));
  }

  removeRow() {
    $(this).closest(".row").detach();
  }

  shortResourceName() {
    return this.resourceName.replace(/\s/g, "");
  }

  isReady() {
    return false;
  }

  retrieveData() {
    const data = [];
    const headers = [];
    const $rows = $(this.table.nativeElement).find('tr:not(:hidden)');

    $rows.each(function () {
      const h = {};
      const $td = $(this).find('td');

      headers.forEach((header, i) => {
        h[header] = $td.eq(i).text();
      });

      data.push(h);
    });

    return data;
  }

}
