import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Category } from '../interface';
import { CategoryService } from '../service/category.service';
import { catchError, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
declare var Swal: any;

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrl: './add.component.scss'
})
export class AddComponent implements OnInit {
  newCategoryForm: FormGroup 
  update:boolean = false

  constructor(private formBuilder: FormBuilder, 
    private route: ActivatedRoute,
    private categoryService: CategoryService){
    this.newCategoryForm = formBuilder.group({})
  }
  ngOnInit(): void {

    this.newCategoryForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
    });
  }


  onSubmit() {
    const category: Category = {
      name: this.newCategoryForm.value.name,
      id: 0, //the api figures this out
      ownerId: 0 //the api figures this out
    }
    this.categoryService.addCategory(category)
      .pipe(
        catchError((error) => {
          console.error('Error adding category:', error);
          return of(null); // Return an observable emitting null to continue the stream
        })
      )
      .subscribe((response: HttpResponse<any> | null) => {
        if (response !== null) {
          Swal.fire({
            title: "Great Job!",
            text: "Category added successfully:",
            icon: "success"
          });
          this.newCategoryForm.reset()
        } 
        else {
          // Handle error
        }
      });
  }

}
