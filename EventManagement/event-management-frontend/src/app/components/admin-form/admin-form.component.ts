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
import imageCompression from 'browser-image-compression';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
	selector: 'app-admin-form',
	standalone: true,
	templateUrl: './admin-form.component.html',
	styleUrls: ['./admin-form.component.css'],
	imports: [ReactiveFormsModule, CommonModule, MatFormFieldModule, MatButtonModule, MatCheckboxModule,
		MatDatepickerModule, MatNativeDateModule, MatInputModule,
		MatIconModule, MatCardModule, MatDividerModule, MatProgressSpinnerModule
	] // <-- Add CommonModule here
	

})
export class AdminFormComponent implements OnInit {
	adminForm: FormGroup;
	adminId?: number;
	isEditing: boolean = false;  // Initialize with false
	allowPasswordEdit: boolean = false;  // Initialize with false
	admin: Admin = {} as Admin;
	
	selectedImageFile: File | null = null;
	imagePreview: string | ArrayBuffer | null = null;
	backendUrl = environment.apiBaseUrl;

	isCompressing: boolean = false;
	isSaving: boolean = false;

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
	    const file = fileInput.files[0];

	    // Check original size
	    if (file.size > 20 * 1024 * 1024) { // 10MB max for raw input
	      alert('Image is too large. Please upload an image under 20 MB.');
	      return;
	    }

	    this.isCompressing = true;

	    const options = {
	      maxSizeMB: 2,
	      maxWidthOrHeight: 1024,
	      useWebWorker: true
	    };

	    imageCompression(file, options)
	      .then((compressedFile) => {
	        if (compressedFile.size > 2 * 1024 * 1024) {
	          alert('Compressed image still exceeds 2MB. Please choose a smaller image.');
	          this.isCompressing = false;
	          return;
	        }

	        this.selectedImageFile = compressedFile;

	        const reader = new FileReader();
	        reader.onload = () => {
	          this.imagePreview = reader.result;
	          this.isCompressing = false;
	        };
	        reader.readAsDataURL(compressedFile);
	      })
	      .catch((error) => {
	        console.error('Image compression failed:', error);
	        this.isCompressing = false;
	      });
	  }
	}


	onImageError(event: Event) {
	  const imgElement = event.target as HTMLImageElement;
	  imgElement.src = 'assets/avatar.png';
	}
	

	uploadProfileImage(): void {
	  if (!this.selectedImageFile || !this.adminId) return;

	  const formData = new FormData();
	  formData.append('image', this.selectedImageFile, this.selectedImageFile.name);

//	  this.adminService.uploadProfileImage(this.adminId, formData).subscribe({
//	    next: (imageUrl) => {
//	      this.adminForm.patchValue({ profileImage: imageUrl });
//	      if (this.admin) this.admin.profileImage = imageUrl;
//	    },
//	    error: (err) => console.error('Image upload failed', err),
//	  });
	  
	  this.adminService.uploadProfileImage(this.adminId, formData).subscribe({
	    next: (res: any) => {
	      const imageUrl = res.imageUrl;
	      console.log('Profile image uploaded:', imageUrl);
	      this.router.navigate(['/admin/list']);
	    },
	    
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

	

	onSubmit() {
	  if (this.adminForm.invalid) {
	    this.adminForm.markAllAsTouched();
	    return;
	  }

	  this.isSaving = true;

	  const admin: Admin = {
	    ...this.adminForm.getRawValue(), // Get raw value to include disabled fields like password
	    id: this.isEditing ? this.admin.id : undefined
	  };

	  const save$ = this.isEditing
	    ? this.adminService.saveAdmin(admin)
	    : this.adminService.saveAdmin(admin);

	  save$.subscribe({
	    next: (savedAdmin) => {
	      if (this.selectedImageFile && savedAdmin.id) {
	        const formData = new FormData();
	        formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
	        this.adminService.uploadProfileImage(savedAdmin.id, formData).subscribe({
	          next: (imageUrl) => {
	            this.isSaving = false;
	            console.log('Profile image uploaded:', imageUrl);
	            this.router.navigate(['/admin/list']);
	          },
	          error: (err) => {
	            this.isSaving = false;
	            console.error('Image upload failed', err);
	            alert('Failed to upload image.');
	          }
	        });
	      } else {
	        this.isSaving = false;
	        this.router.navigate(['/admin/list']);
	      }
	    },
	    error: (err) => {
	      this.isSaving = false;
	      console.error('Failed to save admin:', err);
	      alert('Failed to save admin.');
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
