import { TestBed } from '@angular/core/testing';
import {TicketService} from "./tickets.service";



describe('TicketsService', () => {
  let service: TicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
