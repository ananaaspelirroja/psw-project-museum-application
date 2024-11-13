import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Ticket } from "../../services/models/ticket";
import { OrdersService } from "../../services/services/orders/orders.service";

interface CartItem {
  ticket: Ticket;
  quantity: number;
  totalPrice: number;
}

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent {
  cartItems: CartItem[] = [];
  totalCost: number = 0;

  constructor(private router: Router, private ordersService: OrdersService) {}

  // Aggiunge un articolo al carrello e aggiorna il totale
  addToCart(ticket: Ticket, quantity: number | string): void {
    const item = this.cartItems.find(cartItem => cartItem.ticket.id === ticket.id);

    if (item) {
      item.quantity += Number(quantity);
      item.totalPrice = item.quantity * item.ticket.price!;
    } else {
      this.cartItems.push({
        ticket,
        quantity: Number(quantity),
        totalPrice: ticket.price! * Number(quantity)
      });
    }

    this.calculateTotalCost();

    // Opzionalmente, invia l'articolo al backend per aggiornarlo nel carrello
    this.ordersService.addToCart(ticket.id, Number(quantity)).subscribe(
      () => console.log('Articolo aggiunto al carrello nel backend'),
      error => console.error("Errore durante l'aggiunta al carrello:", error)
    );
  }

  // Aggiorna la quantità di un articolo nel carrello e ricalcola il totale
  updateQuantity(item: CartItem): void {
    item.totalPrice = item.quantity * item.ticket.price!;
    this.calculateTotalCost();
  }

  // Calcola il costo totale di tutti gli articoli nel carrello
  calculateTotalCost(): void {
    this.totalCost = this.cartItems.reduce((acc, item) => acc + item.totalPrice, 0);
  }

  // Svuota il carrello e reimposta il totale
  clearCart(): void {
    this.cartItems = [];
    this.totalCost = 0;
    alert('Carrello svuotato');
  }

  // Esegue l'acquisto inviando i dati del carrello al backend
  purchase(): void {
    const orderData = this.cartItems.map(item => ({
      ticketId: item.ticket.id,
      quantity: item.quantity
    }));

    this.ordersService.createOrder(orderData, this.totalCost).subscribe(
      () => {
        alert('Ordine creato con successo!');
        this.clearCart();
        this.router.navigate(['/my-tickets']);
      },
      error => {
        console.error("Errore durante la creazione dell'ordine:", error);
        alert('Errore durante la creazione dell\'ordine. Riprova.');
      }
    );
  }
}
