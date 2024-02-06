import { Injectable } from '@angular/core';
import { Task } from '../task/interface';
import { SignUp } from '../auth/interface';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private user:SignUp = {
    email: '',
    password: '',
    first_name: '',
    last_name: '',
    role: '',
    password_confirm: ''
  }
  
  private sharedTask: Task = {
    id:0,
    name: '',
    description: '',
    category: {
      name: '',
      id: 0,
      ownerId: 0
    },
    ownerId: '',
    due_date: new Date('2023-12-12'),
    priority: ''
  };

  setTask(task: Task) {
    this.sharedTask = task;
  }
  constructor() { }

  getTask():Task {
    return this.sharedTask
  }

  setUser(user: SignUp) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  getUser(): SignUp  {
    const userString = localStorage.getItem('user');
    if (userString) {
      return JSON.parse(userString);
    }
    return this.user; //empty object
  }

  
}
