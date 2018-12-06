
import { RouterModule }                  from '@angular/router';

import { HomeComponent }                 from './home/home.component';
import { NotFoundComponent }             from './not-found/not-found.component';
import { DocumentationComponent }        from './documentation/documentation.component';


export const routing = RouterModule.forRoot([
	{ path: '', component: HomeComponent },
	{ path: 'not-found', component: NotFoundComponent },
	{ path: 'documentation', component: DocumentationComponent },
	{ path: '**', redirectTo: 'not-found' }
]);
