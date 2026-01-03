import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';


class KanbanService {
}

@Component({
  selector: 'app-kanban-dialog',
  templateUrl: './kanban-dialog.component.html',
  styleUrls: ['./kanban-dialog.component.css']
})
export class KanbanDialogComponent implements OnInit {

  title : string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<KanbanDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private kanbanService: KanbanService) {

      this.form = fb.group({
        title: [this.title, Validators.required]
    });
    }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close();
  }

  save() {
    this.title = this.form.get('title').value;
    if (this.title) {
    //   this.kanbanService.saveNewKanban(this.title).subscribe(
    //
    //     response => {
    //       console.log(response)
    //     }
    //   )
    }
    this.dialogRef.close();
    window.location.reload();
  }

}
