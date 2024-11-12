// ticket-list.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { KeycloakService } from "../../services/services/keycloak/keycloak.service";
import { TicketService } from '../../services/services/tickets/tickets.service';
import { Ticket } from '../../services/models/ticket';
import { Exhibition } from '../../services/models/exhibition';
import {ExhibitionService} from "../../services/services/show-all-exhibitions/exhibition-service.service";

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.scss']
})
export class TicketListComponent implements OnInit {
  tickets: Ticket[] = [];
  isAdmin: boolean = false;
  ticketForm: FormGroup;
  showForm: boolean = false;
  editingTicket: Ticket | null = null;
  newQuantity: number | null = null;

  constructor(
    private ticketService: TicketService,
    private keycloakService: KeycloakService,
    private fb: FormBuilder,
    private exhibitionService: ExhibitionService // Aggiungi ExhibitionService
  ) {
    // Inizializza il form per un nuovo ticket con i campi nullable
    this.ticketForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      exhibition: ['', Validators.required],
      startTime: [null], // Nullable
      endTime: [null]     // Nullable
    });
  }

  async ngOnInit() {
    await this.keycloakService.init();
    this.isAdmin = this.keycloakService.hasRole('ROLE_ADMIN');
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

      // Verifica che 'exhibition' contenga solo l'ID prima di passarlo
      const exhibitionId = newTicket.exhibition as unknown as number; // Assicurati che sia di tipo 'number'

      // Recupera l'exhibition usando l'ID inserito
      this.exhibitionService.getExhibitionById(exhibitionId).subscribe({
        next: (exhibition) => {
          newTicket.exhibition = exhibition; // Assegna l'oggetto Exhibition al ticket

          // Aggiungi il ticket una volta associata l'exhibition
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

  editTicket(ticket: Ticket) {
    this.editingTicket = { ...ticket };
    this.newQuantity = ticket.quantity ?? null; // Imposta null se quantity è undefined
  }


  updateTicket(ticketId: number, newQuantity: number | null) {
    if (newQuantity !== null) {
      this.ticketService.updateTicketQuantity(ticketId, newQuantity).subscribe(updatedTicket => {
        const index = this.tickets.findIndex(t => t.id === updatedTicket.id);
        if (index > -1) {
          this.tickets[index] = { ...updatedTicket };
        }
        alert('Quantità aggiornata con successo');
        this.editingTicket = null;
        this.newQuantity = null;
      });
    } else {
      console.error("Valore della quantità non valido.");
    }
  }

  deleteTicket(ticketId: number) {
    if (confirm('Sei sicuro di voler eliminare questo ticket?')) {
      this.ticketService.deleteTicket(ticketId).subscribe(() => {
        this.tickets = this.tickets.filter(ticket => ticket.id !== ticketId);
        alert('Ticket eliminato con successo');
      });
    }
  }

  cancelEdit() {
    this.editingTicket = null;
    this.newQuantity = null;
  }
}
