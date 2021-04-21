import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGebruiker, Gebruiker } from '../gebruiker.model';
import { GebruikerService } from '../service/gebruiker.service';

@Component({
  selector: 'jhi-gebruiker-update',
  templateUrl: './gebruiker-update.component.html',
})
export class GebruikerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    objectId: [],
    naam: [],
  });

  constructor(protected gebruikerService: GebruikerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gebruiker }) => {
      this.updateForm(gebruiker);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gebruiker = this.createFromForm();
    if (gebruiker.id !== undefined) {
      this.subscribeToSaveResponse(this.gebruikerService.update(gebruiker));
    } else {
      this.subscribeToSaveResponse(this.gebruikerService.create(gebruiker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGebruiker>>): void {
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

  protected updateForm(gebruiker: IGebruiker): void {
    this.editForm.patchValue({
      id: gebruiker.id,
      objectId: gebruiker.objectId,
      naam: gebruiker.naam,
    });
  }

  protected createFromForm(): IGebruiker {
    return {
      ...new Gebruiker(),
      id: this.editForm.get(['id'])!.value,
      objectId: this.editForm.get(['objectId'])!.value,
      naam: this.editForm.get(['naam'])!.value,
    };
  }
}
