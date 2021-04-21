import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOverview } from '../overview.model';
import { OverviewService } from '../service/overview.service';

@Component({
  templateUrl: './overview-delete-dialog.component.html',
})
export class OverviewDeleteDialogComponent {
  overview?: IOverview;

  constructor(protected overviewService: OverviewService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.overviewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
