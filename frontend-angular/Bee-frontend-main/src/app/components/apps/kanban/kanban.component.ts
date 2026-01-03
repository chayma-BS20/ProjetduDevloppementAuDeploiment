// import {Component, Inject, OnInit, PLATFORM_ID, signal} from '@angular/core';
// import { NgbModal, ModalDismissReasons,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
// import {TasksService} from "../../../services/tasks.service";
// import {ToastrService} from "ngx-toastr";
// import {Task} from "../../../model/Task";
// import {KanbanBoardComponent} from "../kanban-board/kanban-board.component";
//
//
// @Component({
//   selector: 'app-kanban',
//   templateUrl: './kanban.component.html',
//   styleUrls: ['./kanban.component.scss']
// })
// export class KanbanComponent implements OnInit {
//   newTaskDescription: string = ''; // Description de la nouvelle tâche
//
//   // Listes de tâches pour chaque colonne
//   todoTasks: any[] = [];
//   inProgressTasks: any[] = [];
//   doneTasks: any[] = [];
//
//   constructor(
//     private tasksService: TasksService,
//     private toastr: ToastrService,
//
//     private modalService: NgbModal // Injectez NgbModal
//   ) {}
//
//
//
//   ngOnInit(): void {
//     // Initialisation des tâches statiques
//     this.todoTasks = [
//       { id: '1', title: 'Tâche 1', status: 'To Do', draggable: true },
//       { id: '2', title: 'Tâche 2', status: 'To Do', draggable: true }
//     ];
//
//     this.inProgressTasks = [
//       { id: '3', title: 'Tâche 3', status: 'In Progress', draggable: true }
//     ];
//
//     this.doneTasks = [
//       { id: '4', title: 'Tâche 4', status: 'Done', draggable: true }
//     ];
//   }
//
//   // Ajouter une nouvelle tâche
//   addTask(): void {
//     if (this.newTaskDescription.trim() !== '') {
//       const newTask = {
//         id: (Math.random() * 1000).toString(), // Générer un ID unique (simulé)
//         title: this.newTaskDescription,
//         status: 'To Do',
//         draggable: true // Permettre le déplacement de la nouvelle tâche
//       };
//
//       // Ajouter la nouvelle tâche à la liste "To Do"
//       this.todoTasks.push(newTask);
//
//       // Réinitialiser la description de la nouvelle tâche
//       this.newTaskDescription = '';
//     }
//   }
//
//   // Supprimer une tâche
//   deleteTask(task: any, status: string): void {
//     // Supprimer la tâche de la liste correspondante en fonction de son statut
//     if (status === 'To Do') {
//       this.todoTasks = this.todoTasks.filter(t => t !== task);
//     } else if (status === 'In Progress') {
//       this.inProgressTasks = this.inProgressTasks.filter(t => t !== task);
//     } else if (status === 'Done') {
//       this.doneTasks = this.doneTasks.filter(t => t !== task);
//     }
//   }
//
//
//
//   openDialogForNewKanban() {
//     const modalRef = this.modalService.open(KanbanBoardComponent, { size: 'sm' }); // Ouvre la popup de notation
//
//   }
//   kanbanList: any;
//   onDrop(event: DragEvent, status: string): void {
//     event.preventDefault();
//     // Implémenter la logique de drop ici
//   }
//
//   onDragOver(event: DragEvent): void {
//     event.preventDefault();
//   }
//
//   onDragStart(event: DragEvent, task: any): void {
//     event.dataTransfer.setData('task', JSON.stringify(task));
//   }
// }
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ToastrService } from 'ngx-toastr';
import { KanbanBoardComponent } from '../kanban-board/kanban-board.component';
import {TasksService} from "../../../services/tasks.service";
import {Board} from "../../../model/Board";
import { Router } from '@angular/router';


@Component({
  selector: 'app-kanban',
  templateUrl: './kanban.component.html',
  styleUrls: ['./kanban.component.scss']
})
export class KanbanComponent implements OnInit {
  kanbanList: Board[];

  constructor(
    private tasksService: TasksService,
    private toastr: ToastrService,
    private modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getKanbanList();
  }

  getKanbanList(): void {
    this.tasksService.getAllBoards().subscribe(
      (boards: Board[]) => {
        this.kanbanList = boards;
      },
      (error) => {
        console.error('Error fetching kanban list:', error);
        this.toastr.error('Failed to fetch kanban list.', 'Error');
      }
    );
  }

  openDialogForNewKanban(): void {
    const modalRef = this.modalService.open(KanbanBoardComponent, { size: 'sm' });
  }
}
