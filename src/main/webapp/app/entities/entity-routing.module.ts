import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'overview',
        data: { pageTitle: 'jhipsterSampleApplicationApp.overview.home.title' },
        loadChildren: () => import('./overview/overview.module').then(m => m.OverviewModule),
      },
      {
        path: 'gebruiker',
        data: { pageTitle: 'jhipsterSampleApplicationApp.gebruiker.home.title' },
        loadChildren: () => import('./gebruiker/gebruiker.module').then(m => m.GebruikerModule),
      },
      {
        path: 'document-type',
        data: { pageTitle: 'jhipsterSampleApplicationApp.documentType.home.title' },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
