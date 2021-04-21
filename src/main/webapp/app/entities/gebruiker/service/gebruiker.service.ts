import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGebruiker, getGebruikerIdentifier } from '../gebruiker.model';

export type EntityResponseType = HttpResponse<IGebruiker>;
export type EntityArrayResponseType = HttpResponse<IGebruiker[]>;

@Injectable({ providedIn: 'root' })
export class GebruikerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/gebruikers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(gebruiker: IGebruiker): Observable<EntityResponseType> {
    return this.http.post<IGebruiker>(this.resourceUrl, gebruiker, { observe: 'response' });
  }

  update(gebruiker: IGebruiker): Observable<EntityResponseType> {
    return this.http.put<IGebruiker>(`${this.resourceUrl}/${getGebruikerIdentifier(gebruiker) as number}`, gebruiker, {
      observe: 'response',
    });
  }

  partialUpdate(gebruiker: IGebruiker): Observable<EntityResponseType> {
    return this.http.patch<IGebruiker>(`${this.resourceUrl}/${getGebruikerIdentifier(gebruiker) as number}`, gebruiker, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGebruiker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGebruiker[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGebruikerToCollectionIfMissing(
    gebruikerCollection: IGebruiker[],
    ...gebruikersToCheck: (IGebruiker | null | undefined)[]
  ): IGebruiker[] {
    const gebruikers: IGebruiker[] = gebruikersToCheck.filter(isPresent);
    if (gebruikers.length > 0) {
      const gebruikerCollectionIdentifiers = gebruikerCollection.map(gebruikerItem => getGebruikerIdentifier(gebruikerItem)!);
      const gebruikersToAdd = gebruikers.filter(gebruikerItem => {
        const gebruikerIdentifier = getGebruikerIdentifier(gebruikerItem);
        if (gebruikerIdentifier == null || gebruikerCollectionIdentifiers.includes(gebruikerIdentifier)) {
          return false;
        }
        gebruikerCollectionIdentifiers.push(gebruikerIdentifier);
        return true;
      });
      return [...gebruikersToAdd, ...gebruikerCollection];
    }
    return gebruikerCollection;
  }
}
