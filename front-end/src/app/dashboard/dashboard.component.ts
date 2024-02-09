import { Component, OnInit } from '@angular/core';
import { TaskService } from '../task/service/task.service';
import { SharedService } from '../service/shared.service';
import { AuthService } from '../auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  tasks: any;

  constructor(private taskService: TaskService, private authService: AuthService, private sharedService: SharedService){
    if(!authService.isLoggedin()){
      this.authService.logout()
    }
  }


  ngOnInit(): void {
    console.log(this.sharedService.getUser())
  }



  // loadTasks() {
  //   this.taskService.getMyTask(1).subscribe(
  //     (data) => {
  //       console.log(data)
  //       this.tasks = data; // Assuming the response is an array of tasks
  //     },
  //     (error) => {
  //       console.error('Error fetching tasks:', error);
  //     }
  //   );
  // }
}

  