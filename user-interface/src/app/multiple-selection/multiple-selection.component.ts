import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { IDropdownSettings, MultiSelectComponent} from 'ng-multiselect-dropdown';

@Component({
  selector: 'app-multiple-selection',
  templateUrl: './multiple-selection.component.html',
  styleUrls: ['./multiple-selection.component.css']
})
export class MultipleSelectionComponent implements OnInit {

  @Input("settings") settings: IDropdownSettings;
  @ViewChild('dropdown') dropdown: MultiSelectComponent;
  @Output("selectedItems") selectedItems = new EventEmitter<Array<any>>();

  constructor() { }

  ngOnInit(): void {
  }

  public init(items: Array<any>, selectedItems: Array<any>) {
    this.addAll(items);
    this.markSelected(selectedItems);
  }

  public add(item: any) {
    const newOption = this.convertToOption(item);
    this.dropdown._data.push(newOption);
  }

  public update(item: any) {
    const updatedOption = this.convertToOption(item);
    this.dropdown._data.forEach(option => {
      if(option.id === updatedOption.id) {
        option.text = updatedOption.text;
      }
    });
  }

  public remove(itemId: any) {
    const option = {id: itemId, text: ""};
    this.dropdown.removeSelected(option);
    const index = this.dropdown._data.map(option => option.id).indexOf(itemId);
    this.dropdown._data.splice(index, 1);
  }

  public save() {
    const convertedSelectedItems = [];
    this.dropdown.selectedItems.forEach(rawItem => {
      const selectedItem = new Object();
      selectedItem[this.settings.idField] = rawItem.id;
      selectedItem[this.settings.textField] = rawItem.text;
      convertedSelectedItems.push(selectedItem);
    });
    this.selectedItems.emit(convertedSelectedItems);
  }

  private addAll(items: Array<any>) : void {
    items.forEach(item => this.add(item));
  }
  
  private markSelected(selectedItems: Array<any>) {
    selectedItems.forEach(item => {
      this.dropdown.addSelected(this.convertToOption(item));
    });
  }

  private convertToOption(item: any) {
    return {id: item[this.settings.idField], text: item[this.settings.textField]}; 
  }
}
