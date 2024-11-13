import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ExhibitionListComponent } from "./pages/exhibition-list/exhibition-list.component";
import {TicketListComponent} from "./pages/ticket-list/ticket-list.component";
import {AuthGuard} from "./services/services/guards/auth.guard";
import {OrdersComponent} from "./pages/orders/orders.component";

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'exhibitions', component: ExhibitionListComponent },
  { path: 'exhibitions/:id', component: ExhibitionListComponent },
  {path: 'tickets', component: TicketListComponent },
  { path: 'my-tickets', component: OrdersComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
