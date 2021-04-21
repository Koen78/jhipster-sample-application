import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOverview, Overview } from '../overview.model';
import { OverviewService } from '../service/overview.service';
import { IGebruiker } from 'app/entities/gebruiker/gebruiker.model';
import { GebruikerService } from 'app/entities/gebruiker/service/gebruiker.service';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';

@Component({
  selector: 'jhi-overview-update',
  templateUrl: './overview-update.component.html',
})
export class OverviewUpdateComponent implements OnInit {
  isSaving = false;

  gebruikersSharedCollection: IGebruiker[] = [];
  documentTypesSharedCollection: IDocumentType[] = [];

  editForm = this.fb.group({
    id: [],
    objectId: [],
    name: [],
    gebruikers: [],
    documentTypes: [],
  });

  constructor(
    protected overviewService: OverviewService,
    protected gebruikerService: GebruikerService,
    protected documentTypeService: DocumentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ overview }) => {
      this.updateForm(overview);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const overview = this.createFromForm();
    if (overview.id !== undefined) {
      this.subscribeToSaveResponse(this.overviewService.update(overview));
    } else {
      this.subscribeToSaveResponse(this.overviewService.create(overview));
    }
  }

  trackGebruikerById(index: number, item: IGebruiker): number {
    return item.id!;
  }

  trackDocumentTypeById(index: number, item: IDocumentType): number {
    return item.id!;
  }

  getSelectedGebruiker(option: IGebruiker, selectedVals?: IGebruiker[]): IGebruiker {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedDocumentType(option: IDocumentType, selectedVals?: IDocumentType[]): IDocumentType {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOverview>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(overview: IOverview): void {
    this.editForm.patchValue({
      id: overview.id,
      objectId: overview.objectId,
      name: overview.name,
      gebruikers: overview.gebruikers,
      documentTypes: overview.documentTypes,
    });

    this.gebruikersSharedCollection = this.gebruikerService.addGebruikerToCollectionIfMissing(
      this.gebruikersSharedCollection,
      ...(overview.gebruikers ?? [])
    );
    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing(
      this.documentTypesSharedCollection,
      ...(overview.documentTypes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gebruikerService
      .query()
      .pipe(map((res: HttpResponse<IGebruiker[]>) => res.body ?? []))
      .pipe(
        map((gebruikers: IGebruiker[]) =>
          this.gebruikerService.addGebruikerToCollectionIfMissing(gebruikers, ...(this.editForm.get('gebruikers')!.value ?? []))
        )
      )
      .subscribe((gebruikers: IGebruiker[]) => (this.gebruikersSharedCollection = gebruikers));

    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing(documentTypes, ...(this.editForm.get('documentTypes')!.value ?? []))
        )
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));
  }

  protected createFromForm(): IOverview {
    return {
      ...new Overview(),
      id: this.editForm.get(['id'])!.value,
      objectId: this.editForm.get(['objectId'])!.value,
      name: this.editForm.get(['name'])!.value,
      gebruikers: this.editForm.get(['gebruikers'])!.value,
      documentTypes: this.editForm.get(['documentTypes'])!.value,
    };
  }
}
