import { TestBed } from '@angular/core/testing';

import { RoutingHistoryService } from './routing-history.service';

describe('RoutingHistoryService', () => {
  let service: RoutingHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoutingHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
