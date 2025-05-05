import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { SidebarComponent } from '../sidebar/sidebar.component'; // adjust path if needed

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [SidebarComponent, RouterModule, MatSidenavModule, MatToolbarModule, MatListModule],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent {}
