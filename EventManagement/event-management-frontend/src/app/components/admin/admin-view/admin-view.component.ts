import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { Admin } from '../../../models/admin.model';
import { CommonModule } from '@angular/common'; // <-- Import CommonModule
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { environment } from '../../../../environments/environment';


@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.css'],
  standalone: true,
    imports: [CommonModule, MatDividerModule, MatCardModule, MatIconModule, MatButtonModule],
})
export class AdminViewComponent implements OnInit {
	admin: Admin = {
	  id: 0,
	  username: '',
	  password: '',
	  name: '',
	  email: '',
	  mobile: '',
	  dateOfBirth: '',
	  profileImage: '',
	  isActive: true,
	  notes: '',
	  lastLoginAt: '',
	  lastLoginIp: ''
	};
	backendUrl = environment.apiBaseUrl;


  constructor(private route: ActivatedRoute, private adminService: AdminService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.adminService.getAdminById(+id).subscribe(data => {
        this.admin = data;
      });
    }
  }
}
