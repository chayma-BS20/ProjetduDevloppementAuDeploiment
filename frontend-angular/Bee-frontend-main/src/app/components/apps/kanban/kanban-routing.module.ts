import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KanbanComponent } from './kanban.component';
import {KanbanSubtasksComponent} from "../kanban-subtasks/kanban-subtasks.component";

// const routes: Routes = [
//   {
//     path: '',
//     children: [
//       {
//         path: '',
//         component: KanbanComponent
//       }
//     ]
//   }
// ];
const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: KanbanComponent
      },
      {
        path: 'kanbans/:id',
        component: KanbanSubtasksComponent
      }
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class KanbanRoutingModule { }
