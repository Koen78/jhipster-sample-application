import { IOverview } from 'app/entities/overview/overview.model';

export interface IDocumentType {
  id?: number;
  objectId?: number | null;
  code?: string | null;
  objectIds?: IOverview[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public objectId?: number | null, public code?: string | null, public objectIds?: IOverview[] | null) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
