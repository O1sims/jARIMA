
import { RouterModule }                  from '@angular/router';

import { HomeComponent }                 from './home/home.component';
import { NotFoundComponent }             from './not-found/not-found.component';


export const routing = RouterModule.forRoot([
	{ path: '', component: HomeComponent },
	{ path: 'not-found', component: NotFoundComponent },
	{ path: '**', redirectTo: 'not-found' }
]);
