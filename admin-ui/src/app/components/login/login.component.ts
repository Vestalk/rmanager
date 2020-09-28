import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {User} from "../../model/User";
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();

  constructor(private snackBar: MatSnackBar,
              private router: Router,
              private auth: AuthService) {
  }

  ngOnInit(): void {
    this.auth.status().subscribe(
      () => {
        this.router.navigateByUrl('/content-container/users');
      },
      () => {
        localStorage.clear();
        this.router.navigateByUrl('/login');
      }
    )
  }

  sendLoginForm(): void {
    this.auth.login(this.user)
      .subscribe(
        resp => {
          localStorage.setItem('tokenAdvMgmgt', resp.token);
          this.router.navigateByUrl("/content-container/users");
        },err => {
          this.snackBar.open('Не правильный логин или пароль', 'OK', {
            duration: 2000,
          });
        });
  }
}
