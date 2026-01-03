import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KanbanComponent } from './kanban.component';
import { KanbanRoutingModule } from './kanban-routing.module';
import {KanbanAllModule} from "@syncfusion/ej2-angular-kanban";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {SharedModule} from "../../../shared/shared.module";
import {MatListModule} from "@angular/material/list";
import {KanbanDialogComponent} from "../kanban-dialog/kanban-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {KanbanBoardComponent} from "../kanban-board/kanban-board.component";
import {KanbanSubtasksComponent} from "../kanban-subtasks/kanban-subtasks.component";
import {AddSubtaskComponent} from "../kanban-subtasks/add-subtask/add-subtask.component";


@NgModule({
  declarations: [KanbanComponent,KanbanDialogComponent,KanbanBoardComponent,KanbanSubtasksComponent,AddSubtaskComponent],
  imports: [
    CommonModule,
    KanbanRoutingModule,
    KanbanAllModule,
    DragDropModule,
    SharedModule,
    MatListModule,
    MatDialogModule,
    MatFormFieldModule,


  ],
  exports: [KanbanComponent] // Si vous prévoyez de réutiliser ce module dans d'autres parties de votre application.
})
export class KanbanModule { }
