import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-step-title',
  templateUrl: './step-title.component.html',
  styleUrls: ['./step-title.component.css']
})
export class StepTitleComponent implements OnInit {

  @Input("title") title: String;

  constructor() { }

  ngOnInit(): void {
  }

}
