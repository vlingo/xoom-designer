import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleSelectionComponent } from './multiple-selection.component';

describe('MultipleSelectionComponent', () => {
  let component: MultipleSelectionComponent;
  let fixture: ComponentFixture<MultipleSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultipleSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
