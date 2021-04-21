import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOverview } from '../overview.model';
import { OverviewService } from '../service/overview.service';
import { OverviewDeleteDialogComponent } from '../delete/overview-delete-dialog.component';

@Component({
  selector: 'jhi-overview',
  templateUrl: './overview.component.html',
})
export class OverviewComponent implements OnInit {
  overviews?: IOverview[];
  isLoading = false;

  constructor(protected overviewService: OverviewService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.overviewService.query().subscribe(
      (res: HttpResponse<IOverview[]>) => {
        this.isLoading = false;
        this.overviews = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOverview): number {
    return item.id!;
  }

  delete(overview: IOverview): void {
    const modalRef = this.modalService.open(OverviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.overview = overview;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
