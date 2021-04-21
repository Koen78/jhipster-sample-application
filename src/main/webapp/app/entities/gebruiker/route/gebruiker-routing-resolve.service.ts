import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGebruiker, Gebruiker } from '../gebruiker.model';
import { GebruikerService } from '../service/gebruiker.service';

@Injectable({ providedIn: 'root' })
export class GebruikerRoutingResolveService implements Resolve<IGebruiker> {
  constructor(protected service: GebruikerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGebruiker> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gebruiker: HttpResponse<Gebruiker>) => {
          if (gebruiker.body) {
            return of(gebruiker.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gebruiker());
  }
}
