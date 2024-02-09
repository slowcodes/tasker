import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { AccessToken, Login, SignUp } from '../interface';
import { HttpResponse } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { catchError } from 'rxjs/internal/operators/catchError';
import { Observable, of, tap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from '../../service/shared.service';
declare var Swal: any


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  signInForm:any
  sweetAlert = Swal

  constructor(private formBuilder: FormBuilder, 
    private router: Router,
    private sharedService: SharedService,
    private authService: AuthService){
    this.signInForm = this.formBuilder.group({});

    if(this.authService.isLoggedin()){
      router.navigateByUrl("/dashboard");
    }
  }
  ngOnInit(): void {
    this.signInForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  submit(): void{
    const login:Login = {
      email:this.signInForm.value.email,
      password:this.signInForm.value.password
    }

    this.authService.signIn(login).pipe(
      tap((response: AccessToken) => {
        this.signInForm.reset();
        
        
        if(response.accessToken !== undefined && response.accessToken!== null){
          //get and save user details in shared service
          this.authService.saveToken(response.accessToken)
          this.authService.getUser().pipe(
            tap((res:SignUp) => {
              this.sharedService.setUser(res)
            
              this.router.navigate(['/dashboard']);
            })
            ,
            catchError((error) => {
            this.sweetAlert.fire({
              icon: 'error',
              title: 'Oops... Unable to find your details',
              text: 'Something went wrong!',
              footer: '<a href="#">This is strange. Lets resolve this for you</a>'
            })
            throw error;
          })
        ).subscribe();
          
        }
      }),
      catchError((error) => {
        this.sweetAlert.fire({
          icon: 'error',
          title: 'Oops... Sorry, Incorrect login details',
          text: 'Something went wrong!',
          footer: '<a href="#">Contact us for quick resolution</a>'
        })
        throw error; // Rethrowing the error to continue propagation
      })
    )
    .subscribe();


  }

}



@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./login.component.scss']
})
export class SignupComponent implements OnInit{
  signUpForm: FormGroup
  sweetAlert = Swal;
  updateProfile = false;
  signup_data: SignUp = {
    email: '',
    password: '',
    first_name: '',
    last_name: '',
    role: '',
    password_confirm: ''
  };

  constructor(private formBuilder: FormBuilder, 
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private sharedService: SharedService,
    private authService: AuthService){
      if(!this.authService.isLoggedin()){
        router.navigateByUrl("/dashboard");
      }
      this.signUpForm = this.formBuilder.group({});
  }
  
  ngOnInit(): void {
    
    if(this.activatedRoute.snapshot.url.join('/') == "user/update"){
      this.updateProfile=true
      console.log("we are running update", this.sharedService.getUser())
    }

    if(this.updateProfile){
      const user: SignUp =this.sharedService.getUser();
      this.signUpForm = this.formBuilder.group({
        first_name: [user.first_name, Validators.required],
        last_name: [user.last_name, Validators.required],
        email: [{value:user.email, disabled: true}, [Validators.required, Validators.email]],
        password: ['', Validators.required],
        confirm_password: ['', Validators.required]
      });
    }
    else{
      this.signUpForm = this.formBuilder.group({
        first_name: ['', Validators.required],
        last_name: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        confirm_password: ['', Validators.required]
      },{ validator: this.passwordMatchValidator });
    }

  }

  passwordMatchValidator: ValidatorFn = (control: AbstractControl): { [key: string]: any } | null => {
    const password = control.get('password');
    const confirmPassword = control.get('confirm_password');

    return password && confirmPassword && password.value !== confirmPassword.value ? { passwordMismatch: true } : null;
  }




  onSubmit() {
     this.signup_data = {
      email: this.signUpForm.value.email,
      password: this.signUpForm.value.password,
      password_confirm: this.signUpForm.value.confirm_password,
      first_name: this.signUpForm.value.first_name,
      last_name: this.signUpForm.value.last_name,
      role: 'USER' //Default role
    }

    if(this.updateProfile){
      this.update();
    }
    else {
      this.signUp();
    }
  }

  update(){

    this.authService.updateUser(this.signup_data).pipe(
      tap((response: HttpResponse<any>) => {
        //console.log('Response status code:', response.status);
        //console.log('User signed up successfully:', response.body); // Response body
        
        if(response.status == 200){

          this.authService.getUser().pipe(
            tap((res:SignUp) => {
              this.sharedService.setUser(res)
              Swal.fire({
                title: "Great Job!",
                text: "Your Tasker account has been successfully udated",
                icon: "success"
              });
              
            })
            ,
            catchError((error) => {
              console.log("Oops... Unable to new your details")
            throw error;
          })
        ).subscribe();
          
        }
      }),
      catchError((error) => {
        // Handle errors
        if (error.status === 404) {
          Swal.fire({
            title: "Error",
            text: "This is likely due to password mismatch",
            icon: "error"
          });
        } else {
          // For other errors, log the error and display a generic error message
          console.error('Error occurred during updateUser:', error);
          this.sweetAlert.fire({
            icon: 'error',
            title: 'Oops... Sorry, this one is on us',
            text: 'Something went wrong!',
            footer: '<a href="#">Contact us for quick resolution</a>'
          });
        }
        // Rethrow the error to continue propagation
        return of(error);
      })
    )
    .subscribe();
  }
  signUp(){
    this.authService.signUp(this.signup_data).pipe(
      tap((response: HttpResponse<any>) => {
        this.signUpForm.reset();
        
        if(response.status == 201){
          Swal.fire({
            title: "Great Job!",
            text: "Your Tasker account has been successfully created",
            icon: "success"
          });
          this.router.navigate(['/']);
        }
      }),
      catchError((error) => {
        this.sweetAlert.fire({
          icon: 'error',
          title: 'Oops... Sorry, this one is on us',
          text: 'Something went wrong!',
          footer: '<a href="#">Contact us for quick resolution</a>'
        })
        //console.error('Error occurred during signup:', error);
        throw error; // Rethrowing the error to continue propagation
      })
    )
    .subscribe();
  }
}
