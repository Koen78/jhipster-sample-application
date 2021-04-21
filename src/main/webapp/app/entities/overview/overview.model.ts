import { IGebruiker } from 'app/entities/gebruiker/gebruiker.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';

export interface IOverview {
  id?: number;
  objectId?: number | null;
  name?: string | null;
  gebruikers?: IGebruiker[] | null;
  documentTypes?: IDocumentType[] | null;
}

export class Overview implements IOverview {
  constructor(
    public id?: number,
    public objectId?: number | null,
    public name?: string | null,
    public gebruikers?: IGebruiker[] | null,
    public documentTypes?: IDocumentType[] | null
  ) {}
}

export function getOverviewIdentifier(overview: IOverview): number | undefined {
  return overview.id;
}
