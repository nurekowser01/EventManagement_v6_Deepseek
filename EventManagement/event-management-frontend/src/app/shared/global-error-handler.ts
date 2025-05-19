import { ErrorHandler, Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  private snackBar = inject(MatSnackBar);

  handleError(error: any): void {
    console.error('Runtime Error:', error);
    this.snackBar.open('Something went wrong!', 'Dismiss', {
      duration: 4000
    });
  }
}
