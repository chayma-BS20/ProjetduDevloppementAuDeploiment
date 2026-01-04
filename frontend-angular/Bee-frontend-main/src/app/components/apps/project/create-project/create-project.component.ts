import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

type Team = {
  teamId: number;
  name: string;
};

type ProjectPayload = {
  projectName: string;
  startDate: string; // "YYYY-MM-DD"
  endDate: string;   // "YYYY-MM-DD"
  status: string;
  budget: number;
};

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent implements OnInit {

  // ✅ adapte si ton backend n'est pas sur 8081
  private readonly API = 'http://localhost:8081/api';

  teams: Team[] = [];
  teamId: number | null = null;

  project: ProjectPayload = {
    projectName: '',
    startDate: '',
    endDate: '',
    status: '',
    budget: 0
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.http.get<Team[]>(`${this.API}/teams`).subscribe({
      next: (data) => (this.teams = data),
      error: (err) => console.error('Error loading teams', err)
    });
  }

  createProject(): void {
    // ✅ validations simples
    if (!this.project.projectName?.trim()) {
      alert('Project name is required');
      return;
    }
    if (!this.teamId) {
      alert('Please choose a team');
      return;
    }
    if (!this.project.startDate || !this.project.endDate) {
      alert('Start date and End date are required');
      return;
    }

    // ✅ POST /api/projects?teamId=1  avec body project
    this.http.post(`${this.API}/projects`, this.project, {
      params: { teamId: String(this.teamId) }
    }).subscribe({
      next: () => {
        alert('Project created successfully ✅');
        this.resetForm();
      },
      error: (err) => {
        console.error(err);
        // message simple pour toi
        alert(err?.error?.message || 'Error creating project');
      }
    });
  }

  resetForm(): void {
    this.project = {
      projectName: '',
      startDate: '',
      endDate: '',
      status: '',
      budget: 0
    };
    this.teamId = null;
  }

  cancel(): void {
    this.resetForm();
  }
}
