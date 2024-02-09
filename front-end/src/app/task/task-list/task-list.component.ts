import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, SortDirection } from '@angular/material/sort';
import { Observable, of as observableOf, catchError, map, merge, startWith, switchMap } from 'rxjs';
import { TaskService } from '../service/task.service';
import { TaskRequestParams, Task } from '../interface';
import { SharedService } from '../../service/shared.service';
import { Router } from '@angular/router';
import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogTitle,
  MatDialogContent,
  MatDialogActions,
  MatDialogClose,
} from '@angular/material/dialog';
import { FormBuilder, Validators } from '@angular/forms';
declare var Swal: any

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements AfterViewInit, OnInit {


  
  searchInput: string = '';
  search_parity = 1;
  filter_parity = 0;






  displayedColumns: string[] = [ 'number', 'created', 'priority', 'category', 'state', 'title','desc'];
  
  data: Task[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  filterForm: any;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;
  
  @ViewChild(MatSort)
  sort!: MatSort;
  
  constructor(private _httpClient: HttpClient, 
    private formBuilder: FormBuilder,
    private sharedService: SharedService,
    private router : Router,
    public dialog: MatDialog,
    private taskService: TaskService) {
      this.filterForm = formBuilder.group({});
  }
  ngOnInit(): void {
    this.filterForm = this.formBuilder.group({
      status: ['', [Validators.required]],
      priority: ['', Validators.required],
      due_date: ['', Validators.required]
    });
  }

  
  
  deleteTask(taskId:number):void {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
    }).then((result:any) => {
      if (result.isConfirmed) {
        this.taskService.deleteTask(taskId)
      .subscribe(
        () => {
          Swal.fire({
            title: "Deleted!",
            text: "Your task has been deleted.",
            icon: "success"
          });
          this.ngAfterViewInit()
        },
        (error) => {
          console.error('Error deleting task:', error);
        }
      );
        
      }
    });
  }

  setAlarm(index:number){
   
    this.openDialog(this.data[index])

  }

  openDialog(task: Task): void {
    const dialogRef = this.dialog.open(TaskReminder, {
      data: task,
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      //this.result = result;
    });
  }


  ngAfterViewInit() {

    var selected_date = this.formatDateString(this.filterForm.value.due_date)

    if(this.filterForm.value.due_date==''){
      selected_date = ''
    }

    const taskRequestParams: TaskRequestParams = {
      priority: this.filterForm.value.priority,
      status: this.filterForm.value.status,
      due_date: selected_date,
      search_text: this.searchInput,
      isSearch: this.search_parity,
      isFilter: this.filter_parity
    }

    console.log(taskRequestParams)


    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.taskService.getMyTask(
            this.paginator.pageIndex, 
            taskRequestParams
          ).pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.isRateLimitReached = data === null;

          if (data === null) {
            return [];
          }

          // Only refresh the result length if there is new data. In case of rate
          // limit errors, we do not want to reset the paginator to zero, as that
          // would prevent users from re-triggering requests.
          this.resultsLength = data.total;
          console.log(data.tasks)
          return data.tasks;
        }),
      )
      .subscribe(data => (this.data = data));
    }

    setUpdateTask(index: number, taskId:number) {
      //[routerLink]="['/task', 'update', row.id]"
      this.sharedService.setTask(this.data[index])
      this.router.navigate(['/task/update/', taskId]);
  
    }
    
    search(event: Event) {
      this.searchInput  = (event.target as HTMLInputElement).value;
      this.search_parity =1
      this.filter_parity =0;
      this.ngAfterViewInit()
    }
  
    filter(){
      this.search_parity =0
      this.filter_parity =1;
      this.ngAfterViewInit()
    }

    formatDateString(dateString:string): any{

      // Create a new Date object from the given date string
      const date = new Date(dateString);

      // Extract date components
      const year = date.getFullYear();
      const month = ('0' + (date.getMonth() + 1)).slice(-2); // Add leading zero if needed
      const day = ('0' + date.getDate()).slice(-2); // Add leading zero if needed
      const hours = ('0' + date.getHours()).slice(-2); // Add leading zero if needed
      const minutes = ('0' + date.getMinutes()).slice(-2); // Add leading zero if needed
      const seconds = ('0' + date.getSeconds()).slice(-2); // Add leading zero if needed

      // Construct the formatted date string
      const formattedDateString = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      return formattedDateString
    }

}


@Component({
  selector: 'alarm-dialog',
  templateUrl: 'alarm-dialog.html',
})
export class TaskReminder{
  constructor(
    public dialogRef: MatDialogRef<TaskReminder>,
    @Inject(MAT_DIALOG_DATA) public data: Task,
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}