import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth/services/auth.service';
declare var  Swal: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {


  constructor(private router: Router,public authService: AuthService){

  }
  title = 'tasker';

  update(){
    this.router.navigateByUrl('/user/update');
    
  }

  logout() {
    Swal.fire({
      title: "Are you sure?",
      text: "All unsaved data will be lost",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, log me out of here!"
    }).then((result:any) => {
      if (result.isConfirmed) {
       this.authService.logout();
      }
    });
  }
}
