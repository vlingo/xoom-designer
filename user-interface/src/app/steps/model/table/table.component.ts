import { Component, OnInit, Input, ViewChild, ElementRef, AfterViewInit, Output, EventEmitter } from '@angular/core';
import * as $ from 'jquery';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit  {
  
  private selectedId: Number;
  @Input() items: Array<any>;
  @Input() resourceName: String;
  @Input() highlightSelected: Boolean = false;
  @Input() disableInclusion: Boolean = false; 
  @Input() itemInclusion = new EventEmitter<any>();
  @Output() itemSelection = new EventEmitter<Number>();
  @Output() itemRemoval = new EventEmitter<Number>(); 
  @Output() itemUpdate = new EventEmitter<Object>(); 
  @ViewChild('table', { read: ElementRef }) table: ElementRef;
  
  ngOnInit(): void {
  }

  addRow() {
    this.itemInclusion.emit({id: Date.now(), name: "New" + this.shortResourceName()});
  }

  selectItem(selectedId: Number, element: ElementRef) {
    this.selectedId = selectedId;
    this.itemSelection.emit(this.selectedId);
    this.handleSelectionEventStyle(element);
  }

  handleSelectionEventStyle(element: ElementRef) {
    if(this.highlightSelected) {
      $(this.table.nativeElement).find(".editable-content").removeClass("selected");
      $(element).addClass("selected");
    }
  }

  onUpdate(id: Number, element: ElementRef) {
    const $nameSpan = $(this.table.nativeElement).find('#' + id);
    const newName = new String($(element).html()).trim();
    this.items.forEach(item => {
      if(item.id === id) {
        item.name = newName;
      }
    });
    this.itemUpdate.emit({"id": id, "name": newName});
  }

  removeItem(id: Number) {
    const index = this.items.map(item => item.id).indexOf(id);
    this.items.splice(index, 1);
    this.itemRemoval.emit(id);
    if(id === this.selectedId) {
      this.selectedId = undefined;
      this.itemSelection.emit(undefined);
    }
  }

  shortResourceName() {
    return this.resourceName.replace(/\s/g, "");
  }

  shouldDisableButton() {
    return this.disableInclusion;
  }
  
}
