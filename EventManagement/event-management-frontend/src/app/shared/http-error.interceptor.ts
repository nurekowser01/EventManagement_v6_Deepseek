import { Injectable, inject } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let message = 'An unexpected error occurred.';
        let panelClass = 'snack-error';

        switch (error.status) {
          case 0:
            message = 'Cannot connect to server.';
            panelClass = 'snack-warn';
            break;
          case 400:
            message = error.error?.message || 'Invalid request.';
            panelClass = 'snack-warn';
            break;
          case 401:
            message = 'Session expired. Please log in again.';
            localStorage.removeItem('auth_token');
            this.router.navigate(['/login']);
            panelClass = 'snack-warn';
            break;
          case 403:
            message = 'You are not allowed to access this resource.';
            panelClass = 'snack-warn';
            // Optional: redirect to dashboard or another safe page
            // this.router.navigate(['/dashboard']);
            break;
          case 404:
            message = 'Resource not found.';
            break;
          case 409:
            message = 'Conflict. This may already exist.';
            break;
          case 500:
            message = 'Internal server error.';
            break;
          case 503:
            message = 'Service unavailable. Please try later.';
            break;
          default:
            message = error.error?.message || `Error ${error.status}`;
        }

        this.snackBar.open(message, 'Close', {
          duration: 5000,
          panelClass
        });

        return throwError(() => error);
      })
    );
  }
}
