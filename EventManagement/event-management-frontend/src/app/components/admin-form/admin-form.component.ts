import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { Admin } from '../../models/admin.model';
import { ReactiveFormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; // <-- Import CommonModule

@Component({
  selector: 'app-admin-form',
  standalone: true,
  templateUrl: './admin-form.component.html',
  imports: [ReactiveFormsModule, CommonModule] // <-- Add CommonModule here
})
export class AdminFormComponent implements OnInit {
  adminForm: FormGroup;
  adminId?: number;
  isEditing: boolean = false;  // Initialize with false
  allowPasswordEdit: boolean = false;  // Initialize with false

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
      profileImageUrl: [''],
      isActive: [true],
      notes: [''],
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.adminId = +id;
        this.isEditing = true;  // Set to true when editing an admin
        this.loadAdmin(this.adminId);
      }
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
        profileImageUrl: admin.profileImageUrl,
        isActive: admin.isActive ?? true,
        notes: admin.notes,
      });

      if (this.isEditing) {
        this.adminForm.get('password')?.disable();  // Disable password field if editing
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
