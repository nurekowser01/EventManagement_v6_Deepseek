import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { AdminViewComponent } from './components/admin/admin-view/admin-view.component';

export const routes: Routes = [
	{
		path: '',
		redirectTo: 'dashboard',
		pathMatch: 'full'
	},
	{
		path: 'login',
		loadComponent: () =>
			import('./components/login/login.component').then(m => m.LoginComponent)
	},



	{
		path: '',
		component: LayoutComponent,
		children: [
			{
				path: 'dashboard',
				loadComponent: () =>
					import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent)
			},
			{
				path: 'admin/list',
				loadComponent: () =>
					import('./components/admin-list/admin-list.component').then(m => m.AdminListComponent)
			},

			{
				path: 'admin/view/:id',
				loadComponent: () =>
					import('./components/admin/admin-view/admin-view.component').then(m => m.AdminViewComponent)
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

		]
	},
	{
		path: '**',
		redirectTo: 'login'
	}
];
