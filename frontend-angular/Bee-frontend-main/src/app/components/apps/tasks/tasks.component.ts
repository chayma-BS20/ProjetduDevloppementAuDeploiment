import { Component, OnInit, ViewChild, Inject, PLATFORM_ID } from '@angular/core';
import { AddTaskComponent } from "./modal/add-task/add-task.component";
import { CreateTagComponent } from "./modal/create-tag/create-tag.component";
import { TasksService } from "../../../services/tasks.service";
import { Task } from '../../../model/Task';
import { ToastrService } from "ngx-toastr";
import { isPlatformBrowser } from "@angular/common";
import { NgbModal, ModalDismissReasons,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {RatingPopupComponent} from "../rating-popup/rating-popup.component"; // Importez NgbModal

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent implements OnInit {
  @ViewChild("addTask") AddTask: AddTaskComponent;
  @ViewChild("createTag") CreateTag: CreateTagComponent;
  tasks: Task[] = []; // Pour stocker les tâches récupérées
  tags: string[] = [];
  task: Task; // Déclarez la variable task

  platformId: Object; // Déclarez la variable platformId
  modalOpen: boolean = false;
  closeResult: string;


  constructor(
    private tasksService: TasksService,
    private toastr: ToastrService,
    @Inject(PLATFORM_ID) platformId: Object, // Injectez PLATFORM_ID
    private modalService: NgbModal // Injectez NgbModal
  ) {
    this.platformId = platformId;
  }

  ngOnInit() {
    this.getTasks(); // Appelez la méthode getTasks lors de l'initialisation du composant
    this.getUniqueTags();

  }

  // Méthode pour récupérer les tâches depuis le service
  getTasks(): void {
    this.tasksService.getAllTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks; // Mettez à jour la liste des tâches avec celles récupérées depuis le service
      },
      error: (err) => {
        console.error('Error fetching tasks:', err);
      }
    });
  }

  // Méthode pour supprimer une tâche
  deleteTask(id: string): void {
    this.tasksService.deleteTask(id).subscribe({
      next: () => {
        // Si la suppression réussit, mettez à jour la liste des tâches en récupérant à nouveau les tâches
        this.getTasks();
        this.toastr.success('Task deleted successfully !', '');
      },
      error: (err) => {
        console.error('Error deleting task:', err);
        this.toastr.error('An error occurred while deleting the task ! ', '');
      }
    });
  }

  getUniqueTags(): void {
    this.tasksService.getUniqueTags().subscribe({
      next: (tags) => {
        this.tags = tags;
      },
      error: (err) => {
        console.error('Error fetching tags:', err);
      }
    });
  }

  // Méthode appelée lorsqu'une nouvelle tâche est ajoutée avec succès
  onTaskAdded(): void {
    this.getTasks(); // Mettez à jour la liste des tâches après l'ajout d'une nouvelle tâche
  }

  // Dans TasksComponent
  filterTasksByTag(tag: string): void {
    this.tasksService.getTasksByTags([tag]).subscribe({
      next: (tasks) => {
        this.tasks = tasks; // Mettez à jour la liste des tâches avec celles filtrées
      },
      error: (err) => {
        console.error('Error fetching tasks by tag:', err);
      }
    });
  }

  filterAllTasksByStatus(status: string) {
    this.tasksService.getAllTasksByStatus(status).subscribe({
      next: (tasks) => {
        this.tasks = tasks; // Mettez à jour la liste des tâches avec celles filtrées
      },
      error: (err) => {
        console.error('Error fetching tasks', err);
      }
    });
  }

  openRatingPopup(task: Task) {
    const modalRef = this.modalService.open(RatingPopupComponent, { size: 'sm' }); // Ouvre la popup de notation
    modalRef.componentInstance.task = task; // Passe la tâche à la popup
  }





  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }


}
