import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditDialogComponent } from './create-edit-dialog.component';

describe('CreateEditDialogComponent', () => {
  let component: CreateEditDialogComponent;
  let fixture: ComponentFixture<CreateEditDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateEditDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
