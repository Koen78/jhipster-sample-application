import { IOverview } from 'app/entities/overview/overview.model';

export interface IGebruiker {
  id?: number;
  objectId?: number | null;
  naam?: string | null;
  objectIds?: IOverview[] | null;
}

export class Gebruiker implements IGebruiker {
  constructor(public id?: number, public objectId?: number | null, public naam?: string | null, public objectIds?: IOverview[] | null) {}
}

export function getGebruikerIdentifier(gebruiker: IGebruiker): number | undefined {
  return gebruiker.id;
}
