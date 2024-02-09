import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import {MatTableModule} from '@angular/material/table';
import { MatSortModule} from '@angular/material/sort';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { MatPaginatorModule} from '@angular/material/paginator';
import { TaskRoutingModule } from './task-routing.module';
import { TaskListComponent, TaskReminder } from './task-list/task-list.component';
import {MatIconModule} from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import {MatMenuModule} from '@angular/material/menu';
import { AddComponent } from './add/add.component';
import {MatDividerModule} from '@angular/material/divider';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { provideNativeDateAdapter } from '@angular/material/core';


@NgModule({
  declarations: [
    TaskListComponent,
    TaskReminder,
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
    MatFormField,
    MatFormFieldModule,
    MatSelectModule,
    MatDialogModule,
    MatInputModule,
    DatePipe,
    ReactiveFormsModule,
    MatDatepickerModule,
  ],
  providers: [
    provideNativeDateAdapter()
  ],
  exports: [
    TaskListComponent
  ]
})
export class TaskModule { }
