import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOverview, getOverviewIdentifier } from '../overview.model';

export type EntityResponseType = HttpResponse<IOverview>;
export type EntityArrayResponseType = HttpResponse<IOverview[]>;

@Injectable({ providedIn: 'root' })
export class OverviewService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/overviews');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(overview: IOverview): Observable<EntityResponseType> {
    return this.http.post<IOverview>(this.resourceUrl, overview, { observe: 'response' });
  }

  update(overview: IOverview): Observable<EntityResponseType> {
    return this.http.put<IOverview>(`${this.resourceUrl}/${getOverviewIdentifier(overview) as number}`, overview, { observe: 'response' });
  }

  partialUpdate(overview: IOverview): Observable<EntityResponseType> {
    return this.http.patch<IOverview>(`${this.resourceUrl}/${getOverviewIdentifier(overview) as number}`, overview, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOverview>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOverview[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOverviewToCollectionIfMissing(overviewCollection: IOverview[], ...overviewsToCheck: (IOverview | null | undefined)[]): IOverview[] {
    const overviews: IOverview[] = overviewsToCheck.filter(isPresent);
    if (overviews.length > 0) {
      const overviewCollectionIdentifiers = overviewCollection.map(overviewItem => getOverviewIdentifier(overviewItem)!);
      const overviewsToAdd = overviews.filter(overviewItem => {
        const overviewIdentifier = getOverviewIdentifier(overviewItem);
        if (overviewIdentifier == null || overviewCollectionIdentifiers.includes(overviewIdentifier)) {
          return false;
        }
        overviewCollectionIdentifiers.push(overviewIdentifier);
        return true;
      });
      return [...overviewsToAdd, ...overviewCollection];
    }
    return overviewCollection;
  }
}
