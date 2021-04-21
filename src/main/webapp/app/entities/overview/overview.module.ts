import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OverviewComponent } from './list/overview.component';
import { OverviewDetailComponent } from './detail/overview-detail.component';
import { OverviewUpdateComponent } from './update/overview-update.component';
import { OverviewDeleteDialogComponent } from './delete/overview-delete-dialog.component';
import { OverviewRoutingModule } from './route/overview-routing.module';

@NgModule({
  imports: [SharedModule, OverviewRoutingModule],
  declarations: [OverviewComponent, OverviewDetailComponent, OverviewUpdateComponent, OverviewDeleteDialogComponent],
  entryComponents: [OverviewDeleteDialogComponent],
})
export class OverviewModule {}
