import { Component } from '@angular/core';
import { AuthService } from '../auth/services/auth.service';
import { SharedService } from '../service/shared.service';

@Component({
  selector: 'app-info-bar',
  templateUrl: './info-bar.component.html',
  styleUrl: './info-bar.component.scss'
})
export class InfoBarComponent {
  name:string;

  constructor(private sharedService: SharedService){
    this.name = this.sharedService.getUser().first_name;
  }
}
