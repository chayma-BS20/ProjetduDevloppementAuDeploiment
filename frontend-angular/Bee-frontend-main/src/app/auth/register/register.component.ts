import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import {AvatarService} from "../../shared/services/avatar.service";
import {DomSanitizer} from "@angular/platform-browser";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {


  avatarUrl: any;
  defaultUrl : string ='https://placehold.jp/150x150.png';
  avatar: any;



  selectedValue = 'thumbs';

  // options = [
  //   { value: 'thumbs', label: 'thumbs' },
  //   { value: 'fun-emoji', label: 'Fun Emoji' },
  //   { value: 'big-smile', label: 'Big Smile' },
  //   { value: 'notionists-neutral', label: ' Notionists Neutral' },
  //
  // ];

  public show: boolean = false;

  currentFile?: File;

  registrationForm: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private fb: FormBuilder,private authService: AuthService,public router: Router, private avatarService: AvatarService, private sanitizer: DomSanitizer) { }

  ngOnInit() {

    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      password: ['', Validators.required],  
      confirmPassword: ['', Validators.required],
      role: ['', Validators.required],
      mfaEnabled: [false] // Checkbox value
    });
  }

  selectFile(event: any): void {
    this.currentFile = event.target.files;
  }

  

  // goLogin() {
  //   this.router.navigate(["/auth/login"]);
  // }

  onSubmit() {
    if (this.registrationForm.valid) {
      console.log(this.registrationForm.value);
      // const formValues = this.registrationForm.value;


      const formData = new FormData();
    formData.append('firstname', this.registrationForm.value["firstname"]);
    formData.append('lastname', this.registrationForm.value["lastName"]);
    formData.append('email', this.registrationForm.value["email"]);
    formData.append('phone', this.registrationForm.value["phone"]);
    formData.append('password', this.registrationForm.value["password"]);
    formData.append('role', this.registrationForm.value["role"]);
    formData.append('mfaEnabled', this.registrationForm.value["mfaEnabled"]);
    formData.append('file', this.currentFile);

      // Submit form data to your backend service here
      this.authService.register(formData).subscribe({
        next: data => {

          console.log(data);
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          // this.router.navigate(["/auth/login"]);

          // Show SweetAlert on successful registration
          Swal.fire({
            title: 'Registration Successful!',
            text: 'Please check your email to confirm your account.',
            icon: 'success'
          });

        },
        error: err => {

          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
        }
      });
    }
  }



  generateAvatar(seed: string) {
    this.avatarService.getAvatar("thumbs",seed).subscribe(blob => {
      const url = URL.createObjectURL(blob);
      this.avatarUrl = this.sanitizer.bypassSecurityTrustUrl(url);
      this.avatar=url;
      console.log(this.avatarUrl);
      console.log(this.avatar);


    });
  }

  // generateAvatar(seed: string) {
  //   this.avatarService.getAvatar(this.selectedValue,seed).subscribe(blob => {
  //     const url = URL.createObjectURL(blob);
  //     this.avatarUrl = this.sanitizer.bypassSecurityTrustUrl(url);
  //     this.avatar=url;
  //     console.log(this.avatarUrl);
  //     console.log(this.avatar);
  //
  //
  //   });
  // }

    // onSelectionChange(event: any) {
  //   this.selectedValue = event.target.value;
  //   // Perform any actions based on the selected value here
  // }
  showPassword() {
    this.show = !this.show;
  }

}
