import {
    HttpEvent,
    HttpHandler,
    HttpRequest,
    HttpErrorResponse,
    HttpInterceptor,
    HttpResponse
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry, tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';
import { LoaderService } from '../service/loader.service';

@Injectable({
    providedIn: 'root'
})
export class HttpRequestHandler implements HttpInterceptor {

    constructor(private toastrService: ToastrService, 
        private loaderService: LoaderService) {

    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.showLoader();
        return next.handle(request).pipe(tap((event: HttpEvent<any>) => { 
            if (event instanceof HttpResponse) {
              this.hideLoader();
            }
          },
            (error: HttpErrorResponse) => {
              this.handleError(error);
          }));
    }

    handleError(error: HttpErrorResponse) {
        this.hideLoader();
        console.log(error);
        this.toastrService.error("The requested operation failed. Please contact the support here: https://github.com/vlingo/vlingo-xoom-starter/issues");
    }

    private showLoader(): void {
        this.loaderService.show();
    }

    private hideLoader(): void {
        this.loaderService.hide();
    }
    
}