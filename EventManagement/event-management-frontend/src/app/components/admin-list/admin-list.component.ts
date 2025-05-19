import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';
import { Router, RouterModule } from '@angular/router';
import { Admin } from '../../models/admin.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';


@Component({
	selector: 'app-admin-list',
	standalone: true,
	imports: [CommonModule, MatTableModule, RouterModule,
		MatIconModule,MatFormFieldModule, MatInputModule, MatCheckboxModule,MatNativeDateModule,
		MatButtonModule, MatDatepickerModule, ConfirmDialogComponent, MatDialogModule
		
	],
	templateUrl: './admin-list.component.html',
	styleUrl: 'admin-list.component.css'
})
export class AdminListComponent implements OnInit {
	admins: any[] = [];
	displayedColumns: string[] = ['username', 'name', 'email', 'isActive', 'actions'];

	constructor(private adminService: AdminService, 
		public router: Router,
		private dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.loadAdmins();
	}

	loadAdmins() {
		this.adminService.getAdmins().subscribe(data => {
			this.admins = data;
		});
	}

	deleteAdmin(id: number) {
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: { message: 'Are you sure you want to delete this admin?' }
		});

		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				this.adminService.deleteAdmin(id).subscribe(() => {
					this.loadAdmins();
				});
			}
		});
	}


	editAdmin(id: number) {
		console.log(id); // Log the ID to check if it's being passed correctly
		this.router.navigate(['/admin/form', id]);
	}

	trackById(index: number, admin: Admin): number | undefined {
		return admin.id;
	}

	addAdmin() {
		this.router.navigate(['/admin/form']);
	}


}
