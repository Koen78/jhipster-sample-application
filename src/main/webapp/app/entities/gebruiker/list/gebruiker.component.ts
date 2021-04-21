import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGebruiker } from '../gebruiker.model';
import { GebruikerService } from '../service/gebruiker.service';
import { GebruikerDeleteDialogComponent } from '../delete/gebruiker-delete-dialog.component';

@Component({
  selector: 'jhi-gebruiker',
  templateUrl: './gebruiker.component.html',
})
export class GebruikerComponent implements OnInit {
  gebruikers?: IGebruiker[];
  isLoading = false;

  constructor(protected gebruikerService: GebruikerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.gebruikerService.query().subscribe(
      (res: HttpResponse<IGebruiker[]>) => {
        this.isLoading = false;
        this.gebruikers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGebruiker): number {
    return item.id!;
  }

  delete(gebruiker: IGebruiker): void {
    const modalRef = this.modalService.open(GebruikerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.gebruiker = gebruiker;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
