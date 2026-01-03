

import { User } from "./User";



export class Board {
  id?: string;
  name: string;
  creationDate: string;
  tags: string[];
  assignedUser?: User;
  // project?: Project;

  constructor() {
    this.name = '';
    this.creationDate = '';

  }
}
