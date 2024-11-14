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
      console.log('Recupero di tutti gli ordini per l\'utente admin...');
      this.ordersService.getAllOrders().subscribe(
        (response) => {
          console.log('Risposta ottenuta per tutti gli ordini:', response); // Log della risposta completa
          this.orders = response;
          console.log('Numero di ordini ricevuti:', this.orders.length); // Log della quantità di ordini ricevuti
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
      console.log('Recupero degli ordini per l\'utente corrente...');
      this.ordersService.getOrdersByUser().subscribe(
        (response) => {
          console.log('Risposta ottenuta per gli ordini dell\'utente:', response); // Log della risposta completa
          this.orders = response;
          console.log('Numero di ordini ricevuti:', this.orders.length); // Log della quantità di ordini ricevuti
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
