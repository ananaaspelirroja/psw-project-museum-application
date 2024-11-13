import { Component, OnInit } from '@angular/core';
import { OrdersService } from '../../services/services/orders/orders.service';

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

  constructor(private ordersService: OrdersService) {}

  ngOnInit(): void {
    this.fetchOrders();
  }

  fetchOrders(): void {
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
