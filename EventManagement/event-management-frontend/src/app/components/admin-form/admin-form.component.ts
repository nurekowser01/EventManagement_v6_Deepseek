import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { Admin, Role } from '../../models/admin.model';
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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RoleService } from '../../services/role.service';
import { MatChipsModule } from '@angular/material/chips';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { environment } from '../../../environments/environment';
import imageCompression from 'browser-image-compression';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-admin-form',
  standalone: true,
  templateUrl: './admin-form.component.html',
  styleUrls: ['./admin-form.component.css'],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatAutocompleteModule,
	ReactiveFormsModule,
	MatTooltipModule
  ]
})
export class AdminFormComponent implements OnInit {
  // Form configuration
  adminForm: FormGroup;
  
  // Admin state management
  adminId?: number;
  isEditing = false;
  admin: Admin = {} as Admin;
  
  // Password fields state
  allowPasswordEdit = false;
  hidePassword = true;
  hideConfirmPassword = true;
  
  // Image handling
  selectedImageFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  originalImagePreview: string | ArrayBuffer | null = null;
  backendUrl = environment.apiBaseUrl;
  isCompressing = false;
  
  // Loading states
  isSaving = false;
  
  // Role management
  allRoles: Role[] = []; // All available roles from API
  selectedRoleTypes: string[] = []; // Currently selected role types (e.g. ["PRINT_REPORT"])

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private roleService: RoleService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    // Initialize form with validation
    this.adminForm = this.fb.group({
      username: ['', Validators.required],
      password: [''],
      confirmPassword: [''],
      name: ['', Validators.required],
      mobile: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      dateOfBirth: [''],
      profileImage: [''],
      isActive: [true],
      notes: [''],
      roles: [[]] // Stores role type strings
    }, { validators: this.passwordsMatchValidator });
  }

  ngOnInit(): void {
    this.loadRoles();
    this.checkEditMode();
  }

  /**
   * Loads all available roles from the API
   */
  private loadRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (roles) => this.allRoles = roles,
      error: (err) => {
        console.error('Role load failed:', err);
        this.snackBar.open('Failed to load roles', 'Close', { duration: 3000 });
      }
    });
  }

  
  
  /**
   * Checks if we're in edit mode and loads admin data if needed
   */
  private checkEditMode(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditing = true;
      this.adminId = +id;
      this.loadAdminData();
    }
  }

  /**
   * Loads admin data for editing
   */
  private loadAdminData(): void {
    this.adminService.getAdminById(this.adminId!).subscribe({
      next: (admin) => {
        this.admin = admin;
        this.initializeSelectedRoles(admin.roles || []);
        
        // Patch form values except password fields
        this.adminForm.patchValue({
          ...admin,
          password: '',
          confirmPassword: ''
        });

        // Disable password fields initially
        this.adminForm.get('password')?.disable();
        this.adminForm.get('confirmPassword')?.disable();

        // Load profile image if exists
        if (admin.profileImage) {
          this.imagePreview = this.backendUrl + admin.profileImage;
          this.originalImagePreview = this.imagePreview;
        }
      },
      error: (err) => {
        console.error('Failed to load admin', err);
        this.snackBar.open('Failed to load admin data', 'Close', { duration: 3000 });
      }
    });
  }

  /**
   * Validates that password and confirm password match
   */
  passwordsMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  /**
   * Enables password fields for editing
   */
  enablePasswordEdit(): void {
    this.allowPasswordEdit = true;
    this.adminForm.get('password')?.enable();
    this.adminForm.get('confirmPassword')?.enable();
    this.adminForm.get('password')?.setValidators([Validators.required]);
    this.adminForm.get('confirmPassword')?.setValidators([Validators.required]);
    this.adminForm.get('password')?.updateValueAndValidity();
    this.adminForm.get('confirmPassword')?.updateValueAndValidity();
  }

  /**
   * Handles image selection and compression
   */
  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (!fileInput.files?.length) return;

    const file = fileInput.files[0];
    
    // Validate file size
    if (file.size > 20 * 1024 * 1024) {
      this.showErrorSnackbar('Image is too large. Please upload under 20MB.');
      return;
    }

    this.compressImage(file);
  }

  /**
   * Compresses the selected image
   */
  private compressImage(file: File): void {
    this.isCompressing = true;
    const options = {
      maxSizeMB: 2,
      maxWidthOrHeight: 1024,
      useWebWorker: true
    };

    imageCompression(file, options)
      .then(compressedFile => {
        if (compressedFile.size > 2 * 1024 * 1024) {
          this.showErrorSnackbar('Compressed image still exceeds 2MB.');
          return;
        }

        this.handleCompressedImage(compressedFile);
      })
      .catch(error => {
        console.error('Image compression failed:', error);
        this.isCompressing = false;
        this.showErrorSnackbar('Image processing failed');
      });
  }

  /**
   * Handles the compressed image file
   */
  private handleCompressedImage(file: File): void {
    this.selectedImageFile = file;
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
      this.isCompressing = false;
    };
    reader.readAsDataURL(file);
  }

  /**
   * Handles image loading errors
   */
  onImageError(event: Event): void {
    const imgElement = event.target as HTMLImageElement;
    imgElement.src = 'assets/avatar.png';
  }

  /**
   * Handles form submission
   */
  onSubmit(): void {
    if (this.adminForm.invalid) {
      this.adminForm.markAllAsTouched();
      return;
    }

    if (this.adminForm.errors?.['passwordMismatch']) {
      this.showErrorSnackbar('Passwords do not match.');
      return;
    }

    this.saveAdmin();
  }

  /**
   * Saves admin data to backend
   */
  private saveAdmin(): void {
    this.isSaving = true;
    const adminData = this.prepareAdminData();

    this.adminService.saveAdmin(adminData).subscribe({
      next: (savedAdmin) => this.handleSaveSuccess(savedAdmin),
      error: () => this.handleSaveError()
    });
  }

  /**
   * Prepares admin data for submission
   */
  private prepareAdminData(): Admin {
    return {
      ...this.adminForm.getRawValue(),
      id: this.isEditing ? this.admin.id : undefined,
      roles: this.selectedRoleTypes
    };
  }

  /**
   * Handles successful admin save
   */
  private handleSaveSuccess(savedAdmin: Admin): void {
    if (this.selectedImageFile && this.imagePreview !== this.originalImagePreview) {
      this.uploadProfileImage(savedAdmin.id!);
    } else {
      this.completeSaveProcess('Admin saved successfully!');
    }
  }

  /**
   * Uploads profile image after successful admin save
   */
  private uploadProfileImage(adminId: number): void {
    const formData = new FormData();
    formData.append('image', this.selectedImageFile!, this.selectedImageFile!.name);

    this.adminService.uploadProfileImage(adminId, formData).subscribe({
      next: () => this.completeSaveProcess('Admin saved and image uploaded successfully!'),
      error: () => this.completeSaveProcess('Admin saved, but image upload failed.', true)
    });
  }

  /**
   * Completes the save process with appropriate feedback
   */
  private completeSaveProcess(message: string, isError = false): void {
    this.isSaving = false;
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: [isError ? 'snackbar-error' : 'snackbar-success']
    });
    this.router.navigate(['/admin/view', this.adminId || 'new']);
  }

  /**
   * Handles save errors
   */
  private handleSaveError(): void {
    this.isSaving = false;
    this.showErrorSnackbar('Failed to save admin.');
  }

  /**
   * Shows error snackbar
   */
  private showErrorSnackbar(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['snackbar-error']
    });
  }

  // Role Management Methods

  /**
   * Initializes selected roles from string array
   */
  private initializeSelectedRoles(roleTypes: string[]): void {
    this.selectedRoleTypes = [...roleTypes];
    this.updateRoleFormValue();
  }

  /**
   * Checks if a role type is selected
   */
  isRoleSelected(roleType: string): boolean {
    return this.selectedRoleTypes.includes(roleType);
  }

  /**
   * Toggles role selection
   */
  toggleRole(roleType: string): void {
    if (this.isRoleSelected(roleType)) {
      this.selectedRoleTypes = this.selectedRoleTypes.filter(r => r !== roleType);
    } else {
      this.selectedRoleTypes = [...this.selectedRoleTypes, roleType];
    }
    this.updateRoleFormValue();
  }

  /**
   * Updates the roles form control value
   */
  private updateRoleFormValue(): void {
    this.adminForm.get('roles')?.setValue(this.selectedRoleTypes);
  }
}