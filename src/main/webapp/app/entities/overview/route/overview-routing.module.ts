import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OverviewComponent } from '../list/overview.component';
import { OverviewDetailComponent } from '../detail/overview-detail.component';
import { OverviewUpdateComponent } from '../update/overview-update.component';
import { OverviewRoutingResolveService } from './overview-routing-resolve.service';

const overviewRoute: Routes = [
  {
    path: '',
    component: OverviewComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OverviewDetailComponent,
    resolve: {
      overview: OverviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OverviewUpdateComponent,
    resolve: {
      overview: OverviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OverviewUpdateComponent,
    resolve: {
      overview: OverviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(overviewRoute)],
  exports: [RouterModule],
})
export class OverviewRoutingModule {}
