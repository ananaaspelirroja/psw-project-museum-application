import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { KeycloakService } from "../../services/services/keycloak/keycloak.service";
import { TicketService } from '../../services/services/tickets/tickets.service';
import { Ticket } from '../../services/models/ticket';
import { ExhibitionService } from "../../services/services/show-all-exhibitions/exhibition-service.service";
import { Router } from '@angular/router';
import { CartComponent } from "../../modules/cart/cart.component";

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.scss']
})
export class TicketListComponent implements OnInit {
  @ViewChild('cart') cart!: CartComponent; // Definisci la referenza del cart component

  tickets: Ticket[] = [];
  isAdmin: boolean = false;
  isUser: boolean = false;
  ticketForm: FormGroup;
  showForm: boolean = false;
  editingTicket: Ticket | null = null;
  newQuantity: number | null = null;
  selectedQuantities: { [ticketId: number]: number } = {}; // Traccia le quantità selezionate

  constructor(
    private ticketService: TicketService,
    private keycloakService: KeycloakService,
    private fb: FormBuilder,
    private exhibitionService: ExhibitionService,
    private router: Router,
  ) {
    this.ticketForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      exhibition: ['', Validators.required],
      startTime: [null],
      endTime: [null]
    });
  }

  async ngOnInit() {
    await this.keycloakService.init();
    this.isAdmin = this.keycloakService.hasRole('ROLE_ADMIN');
    this.isUser = this.keycloakService.hasRole('ROLE_USER');
    this.loadTickets();
  }

  loadTickets() {
    this.ticketService.getAllTickets().subscribe({
      next: (data) => {
        this.tickets = Array.isArray(data) ? data : [];
      },
      error: (err) => {
        console.error("Error fetching tickets:", err);
      }
    });
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
  }

  addTicket() {
    if (this.ticketForm.valid) {
      const newTicket: Ticket = this.ticketForm.value;
      const exhibitionId = newTicket.exhibition as unknown as number;

      this.exhibitionService.getExhibitionById(exhibitionId).subscribe({
        next: (exhibition) => {
          newTicket.exhibition = exhibition;

          this.ticketService.createTicket(newTicket).subscribe({
            next: (createdTicket) => {
              this.tickets.push(createdTicket);
              alert('Ticket creato con successo!');
              this.ticketForm.reset();
              this.showForm = false;
            },
            error: (err) => {
              console.error("Errore creando il ticket:", err);
              alert('Errore nella creazione del ticket');
            }
          });
        },
        error: (err) => {
          console.error("Errore nel recupero dell'exhibition:", err);
          alert('Exhibition non trovata.');
        }
      });
    }
  }

  addToCart(ticket: Ticket): void {
    // @ts-ignore
    const quantity = this.selectedQuantities[ticket.id] || 1; // Quantità selezionata o 1 come default
    this.cart.addToCart(ticket, quantity);
  }

  editTicket(ticket: Ticket) {
    this.editingTicket = { ...ticket };
    this.newQuantity = ticket.quantity ?? null;
  }

  updateTicketQuantity() {
    if (this.editingTicket && this.newQuantity !== null) {
      const updatedTicket = { ...this.editingTicket, quantity: this.newQuantity };

      this.ticketService.updateTicketQuantity(updatedTicket.id!, this.newQuantity).subscribe({
        next: (updatedTicket) => {
          const index = this.tickets.findIndex(t => t.id === updatedTicket.id);
          if (index > -1) {
            this.tickets[index] = { ...updatedTicket };
          }
          alert('Quantità aggiornata con successo');
          this.cancelEdit();
        },
        error: (err) => {
          console.error("Errore aggiornando il ticket:", err);
          alert('Errore nell\'aggiornamento della quantità');
        }
      });
    }
  }

  cancelEdit() {
    this.editingTicket = null;
    this.newQuantity = null;
  }

  deleteTicket(ticketId: number) {
    if (confirm('Sei sicuro di voler eliminare questo ticket?')) {
      this.ticketService.deleteTicket(ticketId).subscribe(() => {
        this.tickets = this.tickets.filter(ticket => ticket.id !== ticketId);
        alert('Ticket eliminato con successo');
      });
    }
  }
}
