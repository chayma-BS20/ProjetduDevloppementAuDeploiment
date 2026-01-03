import {Component, Inject, OnInit, PLATFORM_ID, signal} from '@angular/core';
import { NgbModal, ModalDismissReasons,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {TasksService} from "../../../services/tasks.service";
import {ToastrService} from "ngx-toastr";
import {Task} from "../../../model/Task";
import {KanbanBoardComponent} from "../kanban-board/kanban-board.component";
import {ActivatedRoute, Router} from "@angular/router";
import {AddSubtaskComponent} from "./add-subtask/add-subtask.component";
import {Kanban} from "@syncfusion/ej2-angular-kanban";
import {Subtask} from "../../../model/Subtask";



@Component({
  selector: 'app-kanban-subtasks',
  templateUrl: './kanban-subtasks.component.html',
  styleUrls: ['./kanban-subtasks.component.scss']
})
export class KanbanSubtasksComponent implements OnInit {
  newTaskDescription: string = ''; // Description de la nouvelle tâche
  boardId: string;


  subtasks: Subtask[] = []; // Pour stocker les tâches récupérées

  subtask: Subtask; // Déclarez la variable task

  // Listes de tâches pour chaque colonne
  todoTasks: any[] = [];
  inProgressTasks: any[] = [];
  doneTasks: any[] = [];

  constructor(
    private tasksService: TasksService,
    private toastr: ToastrService,
    private route: ActivatedRoute,

    private modalService: NgbModal, // Injectez NgbModal,
  private router: Router
  ) {}



  ngOnInit(): void {
    // Initialisation des tâches statiques
    this.todoTasks = [
      { id: '1', title: 'Tâche 1', status: 'To Do', draggable: true },
      { id: '2', title: 'Tâche 2', status: 'To Do', draggable: true }
    ];

    this.inProgressTasks = [
      { id: '3', title: 'Tâche 3', status: 'In Progress', draggable: true }
    ];

    this.doneTasks = [
      { id: '4', title: 'Tâche 4', status: 'Done', draggable: true }
    ];

    this.route.params.subscribe(params => {
      this.boardId = params['id'];
      // Faites ce que vous voulez avec l'objet Kanban, comme l'afficher dans le template
    });

    this.getSubTasks(this.boardId);

    this.tasksService.subtaskAdded$.subscribe(() => {
      // Mettre à jour la liste des sous-tâches dès que le signal est reçu
      this.getSubTasks(this.boardId);
    });
  }

  // Ajouter une nouvelle tâche
  addTask(): void {
    if (this.newTaskDescription.trim() !== '') {
      const newTask = {
        id: (Math.random() * 1000).toString(), // Générer un ID unique (simulé)
        title: this.newTaskDescription,
        status: 'To Do',
        draggable: true // Permettre le déplacement de la nouvelle tâche
      };

      // Ajouter la nouvelle tâche à la liste "To Do"
      this.todoTasks.push(newTask);

      // Réinitialiser la description de la nouvelle tâche
      this.newTaskDescription = '';
    }
  }

  // Supprimer une tâche
  deleteTask(task: any, status: string): void {
    // Supprimer la tâche de la liste correspondante en fonction de son statut
    if (status === 'To Do') {
      this.todoTasks = this.todoTasks.filter(t => t !== task);
    } else if (status === 'In Progress') {
      this.inProgressTasks = this.inProgressTasks.filter(t => t !== task);
    } else if (status === 'Done') {
      this.doneTasks = this.doneTasks.filter(t => t !== task);
    }
  }



  openDialogAddSubTask(boardId: string) {
    const modalRef = this.modalService.open(AddSubtaskComponent , { size: 'sm' }); // Ouvre la popup de notation
    modalRef.componentInstance.boardId = boardId
  }
  kanbanList: any;
  onDrop(event: DragEvent, status: string): void {
    event.preventDefault();
    // Implémenter la logique de drop ici
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDragStart(event: DragEvent, task: any): void {
    event.dataTransfer.setData('task', JSON.stringify(task));
  }

  getSubTasks(boardId: string): void {
    this.tasksService.retrieveAllSubTasksByBoard(boardId).subscribe(
      subtasks => {
        this.subtasks = subtasks;
      },
      error => {
        console.error('Erreur lors de la récupération des sous-tâches : ', error);
      }
    );
  }

}
