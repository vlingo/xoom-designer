import { TestBed } from '@angular/core/testing';

import { GenerationSettingsService } from './generation-settings.service';

describe('ApiServiceService', () => {
  let service: GenerationSettingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenerationSettingsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
