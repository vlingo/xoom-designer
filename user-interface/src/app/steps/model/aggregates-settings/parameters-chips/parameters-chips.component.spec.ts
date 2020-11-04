import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParametersChipsComponent } from './parameters-chips.component';

describe('ParametersChipsComponent', () => {
  let component: ParametersChipsComponent;
  let fixture: ComponentFixture<ParametersChipsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParametersChipsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParametersChipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
