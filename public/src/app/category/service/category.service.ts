import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { Observable } from 'rxjs';
import { Category, CategoryResponse } from '../interface';
import { API_URL } from '../../constant';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  headers: HttpHeaders;

  constructor(private httpClient: HttpClient, private authService: AuthService) { 
    // Add the token to the request headers
    //console.log(this.authService.getToken());
    this.headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
      'Content-Type': 'application/json' // Assuming JSON content type for this example
    });
  }
  

  getMyCategories(pageNumber:number) : Observable<CategoryResponse>{
    const headers = this.headers;
    return this.httpClient.get<CategoryResponse>(`${API_URL}/api/v1/category/${pageNumber}`, {headers});
  }

  allMyCategories() : Observable<Category[]>{
    const headers = this.headers;
    return this.httpClient.get<Category[]>(`${API_URL}/api/v1/category`, {headers});
  }

  addCategory(category:Category): Observable<HttpResponse<any>> {
    const headers = this.headers;
    return this.httpClient.post<HttpResponse<any>>(`${API_URL}/api/v1/category`, category, {headers})
  }
}
