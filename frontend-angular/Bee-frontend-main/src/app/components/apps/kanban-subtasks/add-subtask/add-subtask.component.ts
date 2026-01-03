import {Component, Input, OnInit} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import {TasksService} from "../../../../services/tasks.service"
import { Task } from "../../../../model/Task";
import {Subtask} from "../../../../model/Subtask";


@Component({
  selector: 'app-add-subtask',
  templateUrl: './add-subtask.component.html',
  styleUrls: ['./add-subtask.component.scss']
})
export class AddSubtaskComponent implements OnInit {
  tasks: Task[] = [];
  subtaskDescription: string;
  selectedTaskId: string;
  @Input() boardId: string; // Propriété pour recevoir l'ID de la carte
  subtasks: Subtask[] = []; // Pour stocker les tâches récupérées

  subtask: Subtask; // Déclarez la variable task

  constructor(
    public activeModal: NgbActiveModal,
    private taskService: TasksService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    // Récupérer la liste des tâches au chargement du composant
    this.taskService.getAllTasks().subscribe(tasks => {
      this.tasks = tasks;
    });

    console.log(this.boardId);
  }

  onSubmit(): void {
    // // Vérifier si la description de la sous-tâche et l'ID de la tâche sont fournis
    // if (!this.subtaskDescription || !this.selectedTaskId) {
    //   this.toastr.error('Please provide subtask description and select a task.');
    //   return;
    // }

    // Créer un objet Subtask avec les données fournies
    const subtask: Subtask = {
      creationDate: "", status: "",

      description: this.subtaskDescription
      // Ajoutez d'autres propriétés de la sous-tâche selon votre modèle
    };

    // Appeler la méthode addSubTask du service TasksService pour créer la sous-tâche
    this.taskService.addSubTask(subtask, this.selectedTaskId, this.boardId).subscribe(
      (newSubtask) => {
        // Gérer la réponse réussie de l'API
        this.toastr.success('Subtask created successfully.');
        // Fermer la modal une fois que la sous-tâche est créée avec succès
        this.activeModal.close();


      },
      (error) => {
        // Gérer les erreurs lors de la création de la sous-tâche
        this.toastr.error('Failed to create subtask. Please try again.');
        console.error(error); // Afficher l'erreur dans la console pour le débogage
      }
    );
    //this.getSubTasks(this.boardId);
  }

  // getSubTasks(boardId: string): void {
  //   this.taskService.retrieveAllSubTasksByBoard(boardId).subscribe(
  //     subtasks => {
  //       this.subtasks = subtasks;
  //     },
  //     error => {
  //       console.error('Erreur lors de la récupération des sous-tâches : ', error);
  //     }
  //   );
  // }


  onCancel(): void {
    // Ferme la modal sans enregistrer la sous-tâche
    this.activeModal.dismiss();
  }
}
