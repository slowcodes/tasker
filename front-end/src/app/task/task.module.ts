import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import {MatTableModule} from '@angular/material/table';
import {MatSort, MatSortModule, SortDirection} from '@angular/material/sort';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { TaskRoutingModule } from './task-routing.module';
import { AlarmDialog, TaskListComponent } from './task-list/task-list.component';
import {MatIconModule} from '@angular/material/icon';
import { MatButton, MatButtonModule } from '@angular/material/button';
import {MatMenuModule} from '@angular/material/menu';
import { AddComponent } from './add/add.component';
import {MatDivider, MatDividerModule} from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';


@NgModule({
  declarations: [
    TaskListComponent,
    AlarmDialog,
    AddComponent
  ],
  imports: [
    CommonModule,
    TaskRoutingModule,
    MatProgressSpinnerModule, 
    MatTableModule, 
    MatSortModule, 
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatPaginatorModule, 
    MatDividerModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDialogModule,
    MatInputModule,
    DatePipe,
     
    ReactiveFormsModule,
    MatDatepickerModule

  ],
  exports: [
    TaskListComponent
  ]
})
export class TaskModule { }
