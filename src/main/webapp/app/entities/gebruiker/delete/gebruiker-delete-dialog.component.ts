import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGebruiker } from '../gebruiker.model';
import { GebruikerService } from '../service/gebruiker.service';

@Component({
  templateUrl: './gebruiker-delete-dialog.component.html',
})
export class GebruikerDeleteDialogComponent {
  gebruiker?: IGebruiker;

  constructor(protected gebruikerService: GebruikerService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gebruikerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
