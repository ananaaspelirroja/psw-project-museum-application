import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {
  private apiUrlShowPurchases = 'http://localhost:9090/api/my-tickets';
  private apiUrl = 'http://localhost:9090/api/tickets';

  constructor(private http: HttpClient) {}

  // Metodo per ottenere gli articoli nel carrello dell'utente
  getCart(): Observable<any> {
    return this.http.get(`${this.apiUrl}/cart`);
  }

  // Metodo per aggiungere un ticket al carrello
  addToCart(ticketId: number, quantity: number): Observable<any> {
    const url = `http://localhost:9090/api/tickets/add-to-cart?ticketId=${ticketId}&quantity=${quantity}`;
    return this.http.post(url, {});  // Corpo vuoto perché i parametri sono nell'URL
  }


  // Metodo per creare un ordine
  // In OrdersService
  createOrder(orderData: any[], totalAmount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-order`, { orderData, totalAmount });
  }

  // Metodo per ottenere gli ordini di un utente
  getOrdersByUser(): Observable<any> {
    return this.http.get(this.apiUrlShowPurchases);
  }

  getAllOrders(): Observable<any> {
    return this.http.get(`http://localhost:9090/api/orders/all`);
  }


}
