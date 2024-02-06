import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Login, AccessToken } from '../../auth/interface';
import { API_URL } from '../../constant';
import { Task, TaskResponse } from '../interface';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  headers:any;
    

  constructor(private httpClient: HttpClient, private authService: AuthService) { 
    // Add the token to the request headers
    //console.log(this.authService.getToken());
    this.headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
      'Content-Type': 'application/json' // Assuming JSON content type for this example
    });
  }
  

  getMyTask(pageNumber:number, searchTerm:string='') : Observable<TaskResponse>{
    const headers = this.headers;
    return this.httpClient.get<TaskResponse>(`${API_URL}/api/v1/task/${pageNumber}?searchQuery=${searchTerm}`, {headers});
  }

  addTask(data: Task): Observable<HttpResponse<any>> {
    const headers = this.headers;
    return this.httpClient.post<HttpResponse<any>>(`${API_URL}/api/v1/task`, data, {headers})
  }

  deleteTask(id:number): Observable<void> {
    const headers = this.headers;
    return this.httpClient.delete<void>(`${API_URL}/api/v1/task/${id}`, {headers})
  }

  updateTask(data:Task){
    const headers = this.headers;
    return this.httpClient.put<HttpResponse<any>>(`${API_URL}/api/v1/task/${data.id}`, data, {headers})
  }
}
