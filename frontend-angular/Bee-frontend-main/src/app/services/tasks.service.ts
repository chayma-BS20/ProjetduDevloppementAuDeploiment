import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Task } from '../model/Task';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import {User} from "../model/User";
import {Board} from "../model/Board";
import {Subtask} from "../model/Subtask";




@Injectable({
  providedIn: 'root'
})
export class TasksService {
  private apiUrl = 'http://localhost:8080/tasks'; // URL de votre API
  private token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGFpbWEubG90ZmlAZXNwcml0LnRuIiwiaWF0IjoxNzE0MzAwOTExLCJleHAiOjE3MTQzODczMTF9.FRfn24gYFdourN84YvPKHvPXi_Puo9qM84yW4ibZpbg';

  private subtaskAddedSource = new Subject<void>();
  subtaskAdded$ = this.subtaskAddedSource.asObservable();
  constructor(private http: HttpClient) { }


  addTask(task: Task, firstName: string, lastName: string): Observable<Task> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json' // Make sure to set Content-Type header
      })
    };


    // Include firstName and lastName in the URL
    return this.http.post<Task>(`${this.apiUrl}/add/${firstName}/${lastName}`, task, httpOptions);
  }






  // updateTask(id: string,task: Task, firstName: string, lastName: string): Observable<Task> {
  //   const httpOptions = {
  //     headers: new HttpHeaders({
  //       'Authorization': `Bearer ${this.token}`,
  //       'Content-Type': 'application/json' // Make sure to set Content-Type header
  //     })
  //   };
  //
  //   // Include firstName and lastName in the URL
  //   return this.http.put<Task>(`${this.apiUrl}/updateTask/${id}/${firstName}/${lastName}`, task, httpOptions);
  // }

//   updateTask(taskId: string, taskDetails: Task, firstName?: string, lastName?: string): Observable<Task> {
//     const httpOptions = {
//       headers: new HttpHeaders({
//         'Authorization': `Bearer ${this.token}`,
//         'Content-Type': 'application/json' // Assurez-vous de définir l'en-tête Content-Type
//       })
//     };
//
//     // Inclure firstName et lastName dans l'URL s'ils sont fournis
//     const url = firstName && lastName ?
//       `${this.apiUrl}/updateTask/${taskId}/${firstName}/${lastName}` :
//       `${this.apiUrl}/updateTask/${taskId}`;
//
//
// console.log('hhhh'+taskDetails.assignedUser.firstname);
//     return this.http.put<Task>(url, taskDetails, httpOptions);
//   }


  updateTask(id:string ,task: Task, firstName: string, lastName: string): Observable<Task> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json' // Make sure to set Content-Type header
      })
    };


    // Include firstName and lastName in the URL
    return this.http.put<Task>(`${this.apiUrl}/updateTask/${id}/${firstName}/${lastName}`, task, httpOptions);
  }


  // sendNotificationByEmail(email: string): Observable<any> {
  //   const httpOptions = {
  //     headers: new HttpHeaders({
  //       'Authorization': `Bearer ${this.token}`,
  //       'Content-Type': 'text/plain' // Utilisez text/plain pour le contenu
  //     })
  //   };
  //
  //   // Utilisez la même URL de base pour les deux méthodes, mais avec le chemin spécifique pour l'envoi de la notification
  //   return this.http.post(`${this.apiUrl}/send-notification`, email, httpOptions);
  // }

  sendNotification(email: string, task: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      })
    };

    const body = {
      email: email,
      task: {
        title: task.title,
        description: task.description,
        status: task.status,
        priority: task.priority,
        dueDate: task.dueDate,
        creationDate: task.creationDate,
        estimatedDuration: task.estimatedDuration,
        assignedUser: {
          firstName: task.assignedUser.firstName,
          lastName: task.assignedUser.lastName
        }
      }
    };


    return this.http.post<any>(`${this.apiUrl}/send2-notification`, body, httpOptions).pipe(
      catchError((error) => {
        console.error('Error sending notification email:', error);
        return throwError(error);
      })
    );
  }






  getTaskById(id: string): Observable<Task> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<Task>(`${this.apiUrl}/GetTaskById/${id}`, httpOptions);
  }

  retrieveAllSubTasksByBoard(idBoard: string): Observable<Subtask[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<Subtask[]>(`${this.apiUrl}/retrieveAllSubTasksByBoard/${idBoard}`, httpOptions);
  }

  addSubTask(subtask: Subtask, idTask: string, idBoard: string): Observable<Subtask> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json' // Make sure to set Content-Type header
      })
    };


    // Include firstName and lastName in the URL
    return this.http.post<Subtask>(`${this.apiUrl}/addSubTask/${idTask}/${idBoard}`, subtask, httpOptions);
    this.subtaskAddedSource.next();
  }


  getAllTasks(): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<Task[]>(`${this.apiUrl}/GetAllTasks`, httpOptions);
  }


  getUniqueTags(): Observable<string[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<string[]>(`${this.apiUrl}/uniqueTags`, httpOptions);
  }

  deleteTask(id: string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    // Utilisez uniquement l'ID dans l'URL
    return this.http.delete(`${this.apiUrl}/RemoveTask/${id}`, httpOptions)
      .pipe(
        catchError(error => {
          // Afficher un message d'erreur
          console.error('Error in deleting task:', error);
          return throwError('Erreur lors de la suppression de la tâche.');
        })
      );
  }



  // Dans TasksService
  getTasksByTags(tags: string[]): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      }),
      params: {
        tags: tags // Assurez-vous que ceci correspond aux attentes de votre API
      }
    };

    return this.http.get<Task[]>(`${this.apiUrl}/filterByTags`, httpOptions);
  }

  getUsersWithFullName(): Observable<string[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<string[]>(`http://localhost:8080/fullnames`, httpOptions)
      .pipe(
        catchError(error => {
          console.error('Error getting users with full names:', error);
          return throwError('Erreur lors de la récupération des noms complets des utilisateurs.');
        })
      );
  }

  getUserTasks(): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<Task[]>(`${this.apiUrl}/user-tasks`, httpOptions).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération des tâches de l\'utilisateur :', error);
        return throwError('Erreur lors de la récupération des tâches de l\'utilisateur.');
      })
    );
  }

  markTaskAsDone(taskId: string): Observable<Task> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      })
    };

    const url = `${this.apiUrl}/ChangeStatus/${taskId}`;

    return this.http.put<Task>(url, null, httpOptions).pipe(
      catchError(error => {
        console.error('Erreur lors du marquage de la tâche comme "Done" :', error);
        return throwError('Erreur lors du marquage de la tâche comme "Done".');
      })
    );
  }

  updateAllUserTasksAsDone(): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.put<Task[]>(`${this.apiUrl}/updateTasksForLoggedInUser`, {}, httpOptions)
      .pipe(
        catchError(error => {
          console.error('Erreur lors de la mise à jour des tâches de l\'utilisateur :', error);
          return throwError('Erreur lors de la mise à jour des tâches de l\'utilisateur.');
        })
      );
  }



  getTasksByStatus(status: string): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      })
    };

    const url = `${this.apiUrl}/status/${status}`;

    return this.http.get<Task[]>(url, httpOptions).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération des tâches par statut :', error);
        return throwError('Erreur lors de la récupération des tâches par statut.');
      })
    );
  }

  getAllTasksByStatus(status: string): Observable<Task[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      })
    };

    const url = `${this.apiUrl}/filterByStatus/${status}`;

    return this.http.get<Task[]>(url, httpOptions).pipe(
      catchError(error => {
        console.error('Erreur lors de la récupération des tâches par statut :', error);
        return throwError('Erreur lors de la récupération des tâches par statut.');
      })
    );
  }
  getCurrentUser(): Observable<User> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<User>(`${this.apiUrl}/current-user`, httpOptions)
      .pipe(
        catchError(error => {
          console.error('Erreur lors de la récupération de l\'utilisateur actuel :', error);
          return throwError('Erreur lors de la récupération de l\'utilisateur actuel.');
        })
      );
  }







  // updateDueDate(taskId: string, newDueDate: string): Observable<Task> {
  //   const httpOptions = {
  //     headers: new HttpHeaders({
  //       'Authorization': `Bearer ${this.token}`,
  //       'Content-Type': 'application/json'
  //     })
  //   };
  //
  //   const url = `${this.apiUrl}/${taskId}/due-date/${newDueDate}`;
  //
  //   return this.http.patch<Task>(url, null, httpOptions).pipe(
  //     catchError(error => {
  //       console.error('Error updating due date:', error);
  //       return throwError('Error updating due date.');
  //     })
  //   );
  // }
  updateDueDate(taskId: string, newDueDate: Date): Observable<any> {
    const formattedDate = newDueDate.toISOString();
    const url = `${this.apiUrl}/${taskId}/due-date/${formattedDate}`;
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.patch(url, null, httpOptions);

}
  Rating(taskId: string, rating: number): Observable<any> {
    const url = `${this.apiUrl}/Rating/${taskId}/${rating}`;

    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.patch(url, null, httpOptions);
  }





  sendAlertNotification(email: string, task: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json'
      })
    };

    const body = {
      email: email,
      task: {
        title: task.title,
        description: task.description,
        status: task.status,
        priority: task.priority,
        dueDate: task.dueDate,
        creationDate: task.creationDate,
        estimatedDuration: task.estimatedDuration,
        rating: task.rating,


        assignedUser: {
          firstName: task.assignedUser.firstName,
          lastName: task.assignedUser.lastName
        }
      }
    };


    return this.http.post<any>(`${this.apiUrl}/AlertNotification`, body, httpOptions).pipe(
      catchError((error) => {
        console.error('Error sending notification email:', error);
        return throwError(error);
      })
    );
  }
  sendSuccessNotification(email: string, task: any): Observable<any> {
      const httpOptions = {
        headers: new HttpHeaders({
          'Authorization': `Bearer ${this.token}`,
          'Content-Type': 'application/json'
        })
      };

      const body = {
        email: email,
        task: {
          title: task.title,
          description: task.description,
          status: task.status,
          priority: task.priority,
          dueDate: task.dueDate,
          creationDate: task.creationDate,
          estimatedDuration: task.estimatedDuration,
          rating: task.rating,


          assignedUser: {
            firstName: task.assignedUser.firstName,
            lastName: task.assignedUser.lastName
          }
        }
      };


      return this.http.post<any>(`${this.apiUrl}/SuccessNotification`, body, httpOptions).pipe(
        catchError((error) => {
          console.error('Error sending notification email:', error);
          return throwError(error);
        })
      );


  }

  getAllBoards(): Observable<Board[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`
      })
    };

    return this.http.get<Board[]>(`${this.apiUrl}/getAllBoards`, httpOptions);
  }

  addBoard(board:Board, firstName: string, lastName: string): Observable<Board> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${this.token}`,
        'Content-Type': 'application/json' // Make sure to set Content-Type header
      })
    };

    // Include firstName and lastName in the URL
    return this.http.post<Board>(`${this.apiUrl}/addBoard/${firstName}/${lastName}`, board, httpOptions);
  }



  }
