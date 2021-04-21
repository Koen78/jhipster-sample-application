import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GebruikerComponent } from './list/gebruiker.component';
import { GebruikerDetailComponent } from './detail/gebruiker-detail.component';
import { GebruikerUpdateComponent } from './update/gebruiker-update.component';
import { GebruikerDeleteDialogComponent } from './delete/gebruiker-delete-dialog.component';
import { GebruikerRoutingModule } from './route/gebruiker-routing.module';

@NgModule({
  imports: [SharedModule, GebruikerRoutingModule],
  declarations: [GebruikerComponent, GebruikerDetailComponent, GebruikerUpdateComponent, GebruikerDeleteDialogComponent],
  entryComponents: [GebruikerDeleteDialogComponent],
})
export class GebruikerModule {}
