import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOverview, Overview } from '../overview.model';
import { OverviewService } from '../service/overview.service';

@Injectable({ providedIn: 'root' })
export class OverviewRoutingResolveService implements Resolve<IOverview> {
  constructor(protected service: OverviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOverview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((overview: HttpResponse<Overview>) => {
          if (overview.body) {
            return of(overview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Overview());
  }
}
