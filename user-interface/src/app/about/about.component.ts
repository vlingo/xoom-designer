import { Component, OnInit } from '@angular/core';
import { RoutingHistoryService } from '../routing/routing-history.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  constructor(private router: Router, private routingHistoryService: RoutingHistoryService) { 
  }

  ngOnInit(): void {
  }

  close() {
    const previousUrl = this.routingHistoryService.getPreviousUrl();
    this.router.navigate([previousUrl]);
  }
}
