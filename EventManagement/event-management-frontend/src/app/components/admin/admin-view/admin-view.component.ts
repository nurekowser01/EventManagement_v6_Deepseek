import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { Admin, Role, RoleType } from '../../../models/admin.model';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { environment } from '../../../../environments/environment';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RoleService } from '../../../services/role.service';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';

@Component({
	selector: 'app-admin-view',
	templateUrl: './admin-view.component.html',
	styleUrls: ['./admin-view.component.css'],
	standalone: true,
	imports: [
		CommonModule,
		MatDividerModule,
		MatCardModule,
		MatIconModule,
		MatButtonModule,
		MatChipsModule,
		MatTooltipModule,
		MatSnackBarModule
	],
})
export class AdminViewComponent implements OnInit {
	admin: Admin = {
		username: '',
		password: '',
		name: '',
		email: '',
		mobile: '',
		roles: []
	};

	backendUrl = environment.apiBaseUrl;
	roleType = RoleType; // Make enum available in template
	allRoles: Role[] = []; // All available roles from API
	selectedRoleTypes: string[] = [];
	
	constructor(
		private route: ActivatedRoute,
		private adminService: AdminService,
		private roleService: RoleService,
		private snackBar: MatSnackBar

	) { }

	ngOnInit(): void {
		
		this.loadRoles();
		
		const id = this.route.snapshot.paramMap.get('id');
		if (id) {
			this.adminService.getAdminById(+id).subscribe({
				next: (data) => {
					this.admin = {
						...data,
						// Ensure roles is always an array
						roles: data.roles || []
					};
					this.selectedRoleTypes = data.roles || [];
				},
				error: (err) => console.error('Error loading admin', err)
			});
		}
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
	     * Filter Role which is present in the admin I
	     */
	  get filteredRoles(): Role[] {
	    return this.allRoles.filter(role => this.selectedRoleTypes.includes(role.type));
	  }
	  
	// Helper method to get role display name
	getRoleDisplayName(role: string): string {
		switch (role) {
			case RoleType.SUPER_ADMIN: return 'Super Admin';
			case RoleType.ADMIN: return 'Admin';
			case RoleType.MEMBER: return 'Member';
			case RoleType.GUEST: return 'Guest';
			default: return role; // fallback for any custom roles
		}
	}

	// Add these methods to your AdminViewComponent class

	getRoleColor(role: Role): string {
		switch (role) {

			default: return '';
		}
	}


}