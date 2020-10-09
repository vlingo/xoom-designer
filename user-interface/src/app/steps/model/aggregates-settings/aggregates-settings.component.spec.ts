import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggregatesSettingsComponent } from './aggregates-settings.component';

describe('AggregatesSettingsComponent', () => {
  let component: AggregatesSettingsComponent;
  let fixture: ComponentFixture<AggregatesSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggregatesSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggregatesSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
