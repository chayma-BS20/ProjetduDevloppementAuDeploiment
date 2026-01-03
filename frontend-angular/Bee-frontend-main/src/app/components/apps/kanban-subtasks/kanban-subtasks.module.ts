import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KanbanSubtasksComponent } from './kanban-subtasks.component';
import {FormsModule, ReactiveFormsModule,FormBuilder, FormGroup} from '@angular/forms';
import { AddSubtaskComponent } from './add-subtask/add-subtask.component';

@NgModule({
  declarations: [
    KanbanSubtasksComponent,
    AddSubtaskComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
  ]
})
export class KanbanSubtasksModule { }
