import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { RoutingHistoryService } from './routing/routing-history.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private router: Router, private routingHistoryService: RoutingHistoryService) {
    if(routingHistoryService.isFirstAccess()) {
      this.router.navigate(['/settings/context']);
    }
  }
  
}
