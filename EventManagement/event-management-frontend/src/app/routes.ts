import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AdminListComponent } from './components/admin-list/admin-list.component';
import { AdminFormComponent } from './components/admin-form/admin-form.component';

export const  routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'admin/list', component: AdminListComponent },
  { path: 'admin/form/:id', component: AdminFormComponent },
  { path: 'admin/form', component: AdminFormComponent },
  
];
