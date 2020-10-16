import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {MatDialog} from "@angular/material/dialog";
import {User} from "../../model/User";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {

  contentLoaded: boolean = false;

  isAdmin: boolean = false;

  currentUser: User;
  login: string = "";

  constructor(private router: Router,
              private dialog: MatDialog,
              private auth: AuthService,
              private userService: UserService) {
  }

  ngOnInit() {
    Promise.all([
      this.auth.status().subscribe(
        () => {
        },
        () => {
          localStorage.clear();
          this.router.navigateByUrl('/login');
        }),
      this.userService.getUserInfo().subscribe(resp => {
        this.currentUser = resp;
        this.isAdmin = this.currentUser.userRole === 'ADMIN';
        this.login = this.currentUser.login;
      }),
    ]).then(() => this.contentLoaded = true);
  }

  logout(): void {
    this.auth.logout().subscribe(() => {
        localStorage.clear();
        this.router.navigateByUrl("/login");
      }
    );
  }

  routeToUsers() {
    this.router.navigateByUrl("/content-container/users");
  }

  routeToProductMenu() {
    this.router.navigateByUrl("/content-container/product-menu");
  }

  routeToOrderMenu() {
    this.router.navigateByUrl("/content-container/order-menu");
  }
}
