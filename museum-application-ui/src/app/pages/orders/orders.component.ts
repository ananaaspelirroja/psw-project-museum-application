import { Component, OnInit } from '@angular/core';
import { OrdersService } from '../../services/services/orders/orders.service';
import {KeycloakService} from "../../services/services/keycloak/keycloak.service";


@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  hasOrders: boolean = false;
  loading: boolean = true;
  errorMessage: string = '';
  isAdmin: boolean = false;  // Aggiungi una variabile per determinare se l'utente è un admin

  constructor(private ordersService: OrdersService, private keycloakService: KeycloakService) {}

  ngOnInit(): void {
    // Verifica se l'utente ha il ruolo ROLE_ADMIN
    this.isAdmin = this.keycloakService.hasRole('ROLE_ADMIN');
    this.fetchOrders();
  }

  fetchOrders(): void {
    if (this.isAdmin) {
      // Se l'utente è un admin, recupera tutti gli ordini
      this.ordersService.getAllOrders().subscribe(
        (response) => {
          this.orders = response;
          this.hasOrders = this.orders.length > 0;
          this.loading = false;
        },
        (error) => {
          console.error('Errore nel caricamento degli ordini:', error);
          this.errorMessage = 'Errore nel caricamento degli ordini. Riprova più tardi.';
          this.loading = false;
        }
      );
    } else {
      // Se l'utente è un user, recupera solo i suoi ordini
      this.ordersService.getOrdersByUser().subscribe(
        (response) => {
          this.orders = response;
          this.hasOrders = this.orders.length > 0;
          this.loading = false;
        },
        (error) => {
          console.error('Errore nel caricamento degli ordini:', error);
          this.errorMessage = 'Errore nel caricamento degli ordini. Riprova più tardi.';
          this.loading = false;
        }
      );
    }
  }
}
