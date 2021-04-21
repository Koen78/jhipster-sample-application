import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGebruiker } from '../gebruiker.model';

@Component({
  selector: 'jhi-gebruiker-detail',
  templateUrl: './gebruiker-detail.component.html',
})
export class GebruikerDetailComponent implements OnInit {
  gebruiker: IGebruiker | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gebruiker }) => {
      this.gebruiker = gebruiker;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
