import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  {path:'dashboard', component: DashboardComponent},
  
  {path: '', loadChildren: () => import('../app/auth/auth-routing.module').then(mod => mod.AuthRoutingModule)},
  {path: 'task', loadChildren: () => import('../app/task/task-routing.module').then(mod => mod.TaskRoutingModule)},
  {path: 'category', loadChildren: () => import('../app/category/category-routing.module').then(mod => mod.CategoryRoutingModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
