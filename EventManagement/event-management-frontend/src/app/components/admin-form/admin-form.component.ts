import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { Admin } from '../../models/admin.model';

@Component({
  selector: 'app-admin-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-form.component.html',
})
export class AdminFormComponent {
  admin: Admin = {
    id: undefined,  // <--- important! not null
	username: '',
    password: '',
    name: '',
    mobile: '',
    email: ''
  };

  constructor(private adminService: AdminService, public router: Router) {}

  saveAdmin() {
    this.adminService.saveAdmin(this.admin).subscribe(
      () => {
        alert('Admin saved successfully');
        this.router.navigate(['/admin/list']);
      },
      (error) => {
        alert('Error saving admin');
        console.error(error);
      }
    );
  }
}
