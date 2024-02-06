import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../../constant';
import { AccessToken, Login, SignUp } from '../interface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  headers: any;
  constructor(private httpClient: HttpClient, private router: Router) {

  }

  accessHeader():HttpHeaders{
    return this.headers = new HttpHeaders({
      'Authorization': `Bearer ${this.getToken()}`,
      'Content-Type': 'application/json' // Assuming JSON content type for this example
    });
  }

  signIn( data: Login) : Observable<AccessToken>{
    return this.httpClient.post<AccessToken>(`${API_URL}/api/v1/auth/signin`, data)
  }

  signUp(data: SignUp): Observable<HttpResponse<any>> {
    return this.httpClient.post(`${API_URL}/api/v1/auth/signup`, data, { observe: 'response' });
  }

  updateUser(data: SignUp): Observable<HttpResponse<any>> {
    const headers = this.accessHeader();
    return this.httpClient.put(`${API_URL}/api/v1/auth/user`, data, { observe: 'response', headers });
  }

  getUser(): Observable<SignUp> {
    const headers = this.accessHeader();
    return this.httpClient.get<SignUp>(`${API_URL}/api/v1/auth/user`, {headers});
  }

  // Assume token is obtained after successful authentication
  saveToken(token: string) {
    localStorage.setItem('jwt_token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token') ?? null;
  }

  isLoggedin(): boolean {
    if (localStorage.getItem('jwt_token') != null){
      return true;
    }
    return false;
  }
  // Clear token on logout
  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt_token');
    this.router.navigateByUrl("/")
  }

}
