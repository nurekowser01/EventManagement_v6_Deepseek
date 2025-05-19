import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/routes';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { TokenInterceptor } from './app/shared/token.interceptor';
import { HttpErrorInterceptor } from './app/shared/http-error.interceptor'; // 👈
import { ErrorHandler } from '@angular/core';
import { GlobalErrorHandler } from './app/shared/global-error-handler'; // 👈

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    provideAnimations(),

    // Token-based auth interceptor
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },

    // Global error handler interceptor
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    },
	
	{
	      provide: ErrorHandler,
	      useClass: GlobalErrorHandler
	    }
  ]
});
