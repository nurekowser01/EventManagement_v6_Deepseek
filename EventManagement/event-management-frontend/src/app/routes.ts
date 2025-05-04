import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin/list',
    loadComponent: () =>
      import('./components/admin-list/admin-list.component').then(m => m.AdminListComponent)
  },
  {
    path: 'admin/form',
    loadComponent: () =>
      import('./components/admin-form/admin-form.component').then(m => m.AdminFormComponent)
  },
  {
    path: 'admin/form/:id',
    loadComponent: () =>
      import('./components/admin-form/admin-form.component').then(m => m.AdminFormComponent)
  },
  {
    path: '**',  // Fallback route
    redirectTo: 'login'
  }
];

