import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KanbanSubtasksComponent } from './kanban-subtasks.component';

const routes: Routes = [
  {
    path: 'kanbans/:id',
    component: KanbanSubtasksComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class KanbanSubtasksRoutingModule { }
