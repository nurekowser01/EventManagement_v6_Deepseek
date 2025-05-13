import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { Admin } from '../../models/admin.model';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // <-- Import CommonModule
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { environment } from '../../../environments/environment';
import {  ViewEncapsulation } from '@angular/core';

@Component({
	selector: 'app-admin-form',
	standalone: true,
	templateUrl: './admin-form.component.html',
	styleUrls: ['./admin-form.component.css'],
	imports: [ReactiveFormsModule, CommonModule, MatFormFieldModule, MatButtonModule, MatCheckboxModule,
		MatDatepickerModule, MatNativeDateModule, MatInputModule,
		MatIconModule, MatCardModule, MatDividerModule
	] // <-- Add CommonModule here
	

})
export class AdminFormComponent implements OnInit {
	adminForm: FormGroup;
	adminId?: number;
	isEditing: boolean = false;  // Initialize with false
	allowPasswordEdit: boolean = false;  // Initialize with false
	admin!: Admin; // using definite assignment
	selectedFile: File | null = null;
	selectedImageFile: File | null = null;
	imagePreview: string | ArrayBuffer | null = null;
	backendUrl = environment.apiBaseUrl;

	
	constructor(
		private fb: FormBuilder,
		private adminService: AdminService,
		private router: Router,
		private route: ActivatedRoute
	) {
		this.adminForm = this.fb.group({
			username: ['', Validators.required],
			password: ['', Validators.required],  // Ensure it's required for a new admin
			name: ['', Validators.required],
			mobile: ['', Validators.required],
			email: ['', [Validators.required, Validators.email]],
			dateOfBirth: [''],
			profileImage: [''],
			isActive: [true],
			notes: [''],
		});
	}

	
	
	ngOnInit(): void {
	  const id = this.route.snapshot.params['id'];
	  if (id) {
	    this.isEditing = true;
	    this.adminService.getAdminById(+id).subscribe(admin => {
	      this.admin = admin;
	      this.adminForm.patchValue(admin);

	      // Show current image as preview
		  if (admin.profileImage) {
		    this.imagePreview = this.backendUrl + admin.profileImage;
		  }

	      // Disable password field when editing
	      this.adminForm.get('password')?.disable();
	    });
	  }
	}


	
	onFileSelected(event: Event): void {
	  const fileInput = event.target as HTMLInputElement;
	  if (fileInput.files && fileInput.files.length > 0) {
	    this.selectedImageFile = fileInput.files[0];

	    const reader = new FileReader();
	    reader.onload = () => {
	      this.imagePreview = reader.result;
	    };
	    reader.readAsDataURL(this.selectedImageFile);
	  }
	}

	

	uploadProfileImage(): void {
	  if (!this.selectedFile || !this.adminId) return;

	  const formData = new FormData();
	  formData.append('image', this.selectedFile);

	  this.adminService.uploadProfileImage(this.adminId, formData).subscribe({
	    next: (imageUrl) => {
			this.adminForm.patchValue({ profileImage: imageUrl });
			this.admin.profileImage = imageUrl;
	    },
	    error: (err) => console.error('Image upload failed', err),
	  });
	}
	
	enablePasswordEdit() {
		this.allowPasswordEdit = true;
		this.adminForm.get('password')?.enable();  // Allow password field edit
	}

	loadAdmin(id: number): void {
		this.adminService.getAdminById(id).subscribe(admin => {
			this.adminForm.patchValue({
				username: admin.username,
				name: admin.name,
				mobile: admin.mobile,
				email: admin.email,
				dateOfBirth: admin.dateOfBirth,
				profileImage: admin.profileImage,
				isActive: admin.isActive ?? true,
				notes: admin.notes,
			});

			if (this.isEditing) {
				this.adminForm.get('password')?.disable();  // Disable password field if editing
			}
		});
	}

	/*onSubmit(): void {
	  if (this.adminForm.invalid) {
	    this.adminForm.markAllAsTouched(); // Trigger validation messages
	    return;
	  }

	  const adminData: Admin = {
	    ...this.adminForm.value,
	    id: this.adminId
	  };

	  this.adminService.saveAdmin(adminData).subscribe({
	    next: () => this.router.navigate(['/admin/list']),
	    error: (err) => console.error('Failed to save admin:', err)
	  });
	}*/


	/*onSubmit() {
	  if (this.adminForm.invalid) {
	    // If the form is invalid, don't proceed.
	    return;
	  }

	  // Create the admin object from the form values
	  const admin: Admin = {
	    ...this.adminForm.value
	  };

	  // Check if we are in edit mode and the admin has an existing ID
	  if (this.isEditing) {
	    // Update the existing admin
	    this.adminService.saveAdmin(admin).subscribe((savedAdmin) => {
	      // If there's a profile image selected, upload it
	      if (this.selectedImageFile) {
	        // Ensure the ID is defined before passing to the service
	        if (savedAdmin.id) {
	          const formData = new FormData();
	          formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
	          this.adminService.uploadProfileImage(savedAdmin.id, formData).subscribe(
	            (imageUrl) => {
	              // Handle success, maybe update the profile image URL in the form or UI
	              savedAdmin.profileImageUrl = imageUrl;
	              console.log('Profile image uploaded successfully!');
	            },
	            (error) => {
	              console.error('Error uploading profile image:', error);
	            }
	          );
	        } else {
	          console.error('Admin ID is not available for image upload');
	        }
	      }

	      // Do something after the admin is saved successfully
	      console.log('Admin updated successfully:', savedAdmin);
	    }, (error) => {
	      console.error('Error saving admin:', error);
	    });
	  } else {
	    // If we are creating a new admin
	    this.adminService.saveAdmin(admin).subscribe((newAdmin) => {
	      // If there's a profile image selected, upload it
	      if (this.selectedImageFile) {
	        // Ensure the ID is defined before passing to the service
	        if (newAdmin.id) {
	          const formData = new FormData();
	          formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
	          this.adminService.uploadProfileImage(newAdmin.id, formData).subscribe(
	            (imageUrl) => {
	              // Handle success, maybe update the profile image URL in the form or UI
	              newAdmin.profileImageUrl = imageUrl;
	              console.log('Profile image uploaded successfully!');
	            },
	            (error) => {
	              console.error('Error uploading profile image:', error);
	            }
	          );
	        } else {
	          console.error('Admin ID is not available for image upload');
	        }
	      }

	      // Do something after the admin is created successfully
	      console.log('Admin created successfully:', newAdmin);
	    }, (error) => {
	      console.error('Error saving new admin:', error);
	    });
	  }
	}*/

	onSubmit() {
	  if (this.adminForm.invalid) {
	    this.adminForm.markAllAsTouched();
	    return;
	  }

	  const admin: Admin = {
	    ...this.adminForm.value
	  };

	  const save$ = this.isEditing
	    ? this.adminService.saveAdmin({ ...admin, id: this.admin.id })
	    : this.adminService.saveAdmin(admin);

	  save$.subscribe({
	    next: (savedAdmin) => {
	      if (this.selectedImageFile && savedAdmin.id) {
	        const formData = new FormData();
	        formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
	        this.adminService.uploadProfileImage(savedAdmin.id, formData).subscribe({
	          next: (imageUrl) => {
	            console.log('Profile image uploaded:', imageUrl);
	          },
	          error: (err) => console.error('Image upload failed', err)
	        });
	      }
	      this.router.navigate(['/admin/list']);
	    },
	    error: (err) => {
	      console.error('Failed to save admin:', err);
	    }
	  });
	}


	  
	saveAdmin(): void {
		if (this.adminForm.invalid) {
			return;
		}

		const adminData: Admin = {
			...this.adminForm.value,
			id: this.adminId
		};

		this.adminService.saveAdmin(adminData).subscribe(() => {
			this.router.navigate(['/admin/list']);
		});
	}
}
