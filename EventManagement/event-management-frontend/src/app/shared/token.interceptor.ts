import { Injectable } from '@angular/core';
import {
  HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    console.log('Token interceptor:', token);

    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

	return next.handle(req).pipe(
	  catchError((error: HttpErrorResponse) => {
	    if (error.status === 401) {
	      // Only redirect on 401 (unauthorized / expired token)
	      this.authService.logout(); // Clear token
	      this.router.navigate(['/login']);
	    }
	    return throwError(() => error);
	  })
	);
  }
}
