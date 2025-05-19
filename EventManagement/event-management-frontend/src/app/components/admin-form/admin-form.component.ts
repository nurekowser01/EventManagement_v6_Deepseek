// admin-form.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { Admin } from '../../models/admin.model';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
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
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-form',
  standalone: true,
  templateUrl: './admin-form.component.html',
  styleUrls: ['./admin-form.component.css'],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatFormFieldModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatSnackBarModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    MatProgressSpinnerModule
  ]
})
export class AdminFormComponent implements OnInit {
  adminForm: FormGroup;
  adminId?: number;
  isEditing = false;
  allowPasswordEdit = false;
  admin: Admin = {} as Admin;

  selectedImageFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  backendUrl = environment.apiBaseUrl;
  originalImagePreview: string | ArrayBuffer | null = null;

  isCompressing = false;
  isSaving = false;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.adminForm = this.fb.group(
      {
        username: ['', Validators.required],
        password: [''],
        confirmPassword: [''],
        name: ['', Validators.required],
        mobile: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        dateOfBirth: [''],
        profileImage: [''],
        isActive: [true],
        notes: ['']
      },
      { validators: this.passwordsMatchValidator }
    );
  }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditing = true;
      this.adminId = +id;

      this.adminService.getAdminById(this.adminId).subscribe(admin => {
        this.admin = admin;
        this.adminForm.patchValue(admin);
        this.adminForm.get('password')?.disable();
        this.adminForm.get('confirmPassword')?.disable();

        if (admin.profileImage) {
          
		  this.imagePreview = this.backendUrl + admin.profileImage;
		  this.originalImagePreview = this.imagePreview;
        }
      });
    }
  }

  passwordsMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  enablePasswordEdit() {
    this.allowPasswordEdit = true;
    this.adminForm.get('password')?.enable();
    this.adminForm.get('confirmPassword')?.enable();
    this.adminForm.get('password')?.setValidators([Validators.required]);
    this.adminForm.get('confirmPassword')?.setValidators([Validators.required]);
    this.adminForm.get('password')?.updateValueAndValidity();
    this.adminForm.get('confirmPassword')?.updateValueAndValidity();
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      const file = fileInput.files[0];

      if (file.size > 20 * 1024 * 1024) {
        this.snackBar.open('Image is too large. Please upload under 20MB.', 'Close', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
        return;
      }

      this.isCompressing = true;

      const options = {
        maxSizeMB: 2,
        maxWidthOrHeight: 1024,
        useWebWorker: true
      };

      imageCompression(file, options)
        .then(compressedFile => {
          if (compressedFile.size > 2 * 1024 * 1024) {
            this.snackBar.open('Compressed image still exceeds 2MB.', 'Close', {
              duration: 3000,
              panelClass: ['snackbar-error']
            });
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
        .catch(error => {
          console.error('Image compression failed:', error);
          this.isCompressing = false;
        });
    }
  }

  onImageError(event: Event) {
    const imgElement = event.target as HTMLImageElement;
    imgElement.src = 'assets/avatar.png';
  }

  onSubmit() {
    if (this.adminForm.invalid) {
      this.adminForm.markAllAsTouched();
      return;
    }

    if (this.adminForm.errors?.['passwordMismatch']) {
      this.snackBar.open('Passwords do not match.', 'Close', {
        duration: 3000,
        panelClass: ['snackbar-error']
      });
      return;
    }

    this.isSaving = true;

    const admin: Admin = {
      ...this.adminForm.getRawValue(),
      id: this.isEditing ? this.admin.id : undefined
    };

    this.adminService.saveAdmin(admin).subscribe({
      next: (savedAdmin) => {
		
		const isImageChanged = this.imagePreview !== this.originalImagePreview;

        if (this.selectedImageFile && isImageChanged && savedAdmin.id) {
          const formData = new FormData();
          formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
		  
          this.adminService.uploadProfileImage(savedAdmin.id, formData).subscribe({
            next: () => {
              this.isSaving = false;
              this.snackBar.open('Admin saved and image uploaded successfully!', 'Close', {
                duration: 3000,
                panelClass: ['snackbar-success']
              });
              this.router.navigate(['/admin/view', savedAdmin.id]);
            },
            error: () => {
              this.isSaving = false;
              this.snackBar.open('Admin saved, but image upload failed.', 'Close', {
                duration: 3000,
                panelClass: ['snackbar-error']
              });
              this.router.navigate(['/admin/view', savedAdmin.id]);
            }
          });
        } else {
          this.isSaving = false;
          this.snackBar.open('Admin saved successfully!', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-success']
          });
          this.router.navigate(['/admin/view', savedAdmin.id]);
        }
      },
      error: () => {
        this.isSaving = false;
        this.snackBar.open('Failed to save admin.', 'Close', {
          duration: 3000,
          panelClass: ['snackbar-error']
        });
      }
    });
  }
}
