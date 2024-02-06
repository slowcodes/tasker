import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, Inject, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, SortDirection } from '@angular/material/sort';
import { Observable, of as observableOf, catchError, map, merge, startWith, switchMap } from 'rxjs';
import { TaskService } from '../service/task.service';
import { Task } from '../interface';
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
declare var Swal: any

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements AfterViewInit {


  setUpdateTask(index: number, taskId:number) {
    //[routerLink]="['/task', 'update', row.id]"
    this.sharedService.setTask(this.data[index])
    this.router.navigate(['/task/update/', taskId]);

  }
  searchInput: string = '';


  search(event: Event) {
    this.searchInput  = (event.target as HTMLInputElement).value;
    this.ngAfterViewInit()
  }



  displayedColumns: string[] = [ 'number', 'created', 'priority', 'state', 'title','desc'];
  
  data: Task[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;
  
  @ViewChild(MatSort)
  sort!: MatSort;
  
  constructor(private _httpClient: HttpClient, 
    private sharedService: SharedService,
    private router : Router,
    public dialog: MatDialog,
    private taskService: TaskService) {}

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
    const dialogRef = this.dialog.open(AlarmDialog, {
      data: task,
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      //this.result = result;
    });
  }



  ngAfterViewInit() {

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.taskService.getMyTask(
            this.paginator.pageIndex, this.searchInput
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
          return data.tasks;
        }),
      )
      .subscribe(data => (this.data = data));
    }
}


@Component({
  selector: 'alarm-dialog',
  templateUrl: 'alarm-dialog.html',
})
export class AlarmDialog{
  constructor(
    public dialogRef: MatDialogRef<AlarmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Task,
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}