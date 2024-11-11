import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ExhibitionListComponent } from "./pages/exhibition-list/exhibition-list.component";

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'exhibitions', component: ExhibitionListComponent },
  { path: 'exhibitions/:id', component: ExhibitionListComponent } // Rotta con parametro ID per mostrare i dettagli
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
