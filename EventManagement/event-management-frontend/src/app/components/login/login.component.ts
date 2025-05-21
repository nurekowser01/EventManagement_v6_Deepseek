import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
	selector: 'app-login',
	standalone: true,
	imports: [MatIconModule, CommonModule, ReactiveFormsModule, MatSnackBarModule,
		MatCardModule, MatInputModule, MatButtonModule, FormsModule]
	,
	templateUrl: './login.component.html',
})
export class LoginComponent {
	loginForm!: FormGroup;

	constructor(private fb: FormBuilder, private authService: AuthService,
		private snackBar: MatSnackBar,
		public router: Router) { }

	ngOnInit(): void {
		this.loginForm = this.fb.group({
			username: ['', [Validators.required]],
			password: ['', [Validators.required]],
		});
	}

	onLogin(): void {
		if (this.loginForm.valid) {
			const { username, password } = this.loginForm.value;
			this.authService.login(username, password).subscribe(
				() => {
					this.router.navigate(['/admin/list']);
				},
				(error) => {
					let errorMessage = 'Login failed. Please try again.';
					console.error('Login error:', error);

					// Check if backend returned your structured ApiErrorResponse JSON
					if (error.error) {
						// error.error should be your ApiErrorResponse object
						if (typeof error.error === 'object' && error.error.message) {
							errorMessage = error.error.message;  // <-- Use the message field
						} else if (typeof error.error === 'string') {
							errorMessage = error.error;
						}
					}

					this.snackBar.open(errorMessage, 'Close', {
						duration: 4000,
						panelClass: ['mat-warn'],
					});
				}
			);
		}
	}



}
