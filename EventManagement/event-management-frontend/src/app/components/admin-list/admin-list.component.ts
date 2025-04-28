import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-list.component.html',
})
export class AdminListComponent implements OnInit {
  admins: any[] = [];

  constructor(private adminService: AdminService, public router: Router) {}

  ngOnInit(): void {
    this.loadAdmins();
  }

  loadAdmins() {
    this.adminService.getAdmins().subscribe(data => {
      this.admins = data;
    });
  }

  deleteAdmin(id: number) {
    this.adminService.deleteAdmin(id).subscribe(() => {
      this.loadAdmins();
    });
  }

  editAdmin(id: number) {
	console.log(id); // Log the ID to check if it's being passed correctly
    this.router.navigate(['/admin/form', id]);
  }
}
