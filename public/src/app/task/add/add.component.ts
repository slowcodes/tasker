import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../category/service/category.service';
import { Category } from '../../category/interface';
import { catchError, of } from 'rxjs';
import { Task } from '../interface';
import { TaskService } from '../service/task.service';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from '../../service/shared.service';
import { AuthService } from '../../auth/services/auth.service';
declare var Swal:any;


@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrl: './add.component.scss'
})
export class AddComponent implements OnInit {
  newTaskForm: any;
  updateTask: boolean = false;
  myCategories: Category[] | undefined
  swal = Swal;
  constructor(private formBuilder: FormBuilder, 
    private route: ActivatedRoute, private authService:AuthService,
    private router: Router,
    private taskService: TaskService,
    private sharedService: SharedService,
    private categoryService: CategoryService){
    
    this.newTaskForm = formBuilder.group({})
    if(!this.authService.isLoggedin()){
      this.authService.logout()
    }
  }
  
  
  ngOnInit(): void {
    const id = this.route.snapshot.params?.['id'];
    
    if(id != null){
      this.updateTask = true;
      console.log("We are updating", id)
    }
    this.categoryService.allMyCategories().pipe(
      catchError(error => {
        console.error('Error fetching categories:', error);
        return of([]); // Return an empty array in case of error
      })
    ).subscribe(
      (categories: Category[]) => {
        this.myCategories = categories;
        if(categories.length ==0){
          this.swal.fire({
            icon: "error",
            title: "Oops...",
            text: "You have to create a category!",
            footer: '<a href="/category/new">Click here to create one</a>'
          });
        }
      }
    );

    if(this.updateTask){
      this.newTaskForm = this.formBuilder.group({
        name: [this.sharedService.getTask().name, Validators.required],
        description: [this.sharedService.getTask().description, Validators.required],
        priority: [this.sharedService.getTask().priority, Validators.required],
        category: [this.sharedService.getTask().category.id, Validators.required],
        due_date: [this.sharedService.getTask().due_date, Validators.required],
        status: [this.sharedService.getTask().status, Validators.required]
      });
    }
    else{
      this.newTaskForm = this.formBuilder.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        priority: ['', Validators.required],
        category: ['', Validators.required],
        due_date: ['', Validators.required],
      });
    }
    
  }


  onSubmit(): void {
    const task : Task = {
      id: this.sharedService.getTask().id,
      name: this.newTaskForm.value.name,
      description: this.newTaskForm.value.description,
      category: {
        name: '',
        id: this.newTaskForm.value.category,
        ownerId: 0
      },
      ownerId: '',
      due_date: this.newTaskForm.value.due_date,
      priority: this.newTaskForm.value.priority,
      status: this.newTaskForm.value.status
    }

    if(this.updateTask){
      this.sumbmitUpdated(task);
    }
    else{
      this.submitNewTask(task);
    }

  }

  submitNewTask(task: Task){
    this.taskService.addTask(task)
      .pipe(
        catchError((error) => {
          console.error('Error adding task:', error);
          return of(null); // Return an observable emitting null to continue the stream
        })
      )
      .subscribe((response: HttpResponse<any> | null) => {
        if (response !== null) {
          Swal.fire({
            title: "Great Job!",
            text: "Task saved successfully:",
            icon: "success"
          });
          this.newTaskForm.reset()
        } 
        else {
          // Handle error
        }
      });
  }
  sumbmitUpdated(task: Task){
    this.taskService.updateTask(task)
      .pipe(
        catchError((error) => {
          console.error('Error updating task:', error);
          return of(null); // Return an observable emitting null to continue the stream
        })
      )
      .subscribe((response: HttpResponse<any> | null) => {
        if (response !== null) {
          Swal.fire({
            title: "Great Job!",
            text: "Task updated successfully:",
            icon: "success"
          });
          this.router.navigateByUrl('/dashboard')
        } 
        else {
          // Handle error
        }
      });
  }
  
}
