import { Component, OnInit } from '@angular/core';
import {OrdersService} from "../../services/services/orders/orders.service";
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
  isAdmin: boolean = false;

  constructor(
    private ordersService: OrdersService,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.keycloakService.hasRole('ROLE_ADMIN');
    this.fetchOrders();
  }

  fetchOrders(): void {
    if (this.isAdmin) {
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
