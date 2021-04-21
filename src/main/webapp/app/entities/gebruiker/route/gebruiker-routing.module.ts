import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GebruikerComponent } from '../list/gebruiker.component';
import { GebruikerDetailComponent } from '../detail/gebruiker-detail.component';
import { GebruikerUpdateComponent } from '../update/gebruiker-update.component';
import { GebruikerRoutingResolveService } from './gebruiker-routing-resolve.service';

const gebruikerRoute: Routes = [
  {
    path: '',
    component: GebruikerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GebruikerDetailComponent,
    resolve: {
      gebruiker: GebruikerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GebruikerUpdateComponent,
    resolve: {
      gebruiker: GebruikerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GebruikerUpdateComponent,
    resolve: {
      gebruiker: GebruikerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gebruikerRoute)],
  exports: [RouterModule],
})
export class GebruikerRoutingModule {}
