import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOverview } from '../overview.model';

@Component({
  selector: 'jhi-overview-detail',
  templateUrl: './overview-detail.component.html',
})
export class OverviewDetailComponent implements OnInit {
  overview: IOverview | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ overview }) => {
      this.overview = overview;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
