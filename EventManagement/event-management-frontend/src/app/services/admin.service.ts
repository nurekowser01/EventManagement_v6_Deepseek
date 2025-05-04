import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Admin } from '../models/admin.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private readonly apiUrl = 'http://localhost:8080/api/admins';  // Mark as readonly

  constructor(private http: HttpClient) {}

  getAdmins(): Observable<Admin[]> {
    return this.http.get<Admin[]>(this.apiUrl);
  }

  getAdminById(id: number): Observable<Admin> {
    return this.http.get<Admin>(`${this.apiUrl}/${id}`);
  }

  saveAdmin(admin: Admin): Observable<Admin> {
    if (admin.id) {
      // Update – send full object including id
      return this.http.put<Admin>(`${this.apiUrl}/${admin.id}`, admin);
    } else {
      // Create – remove id before sending
      const adminData = { ...admin };
      delete adminData.id;
      return this.http.post<Admin>(this.apiUrl, adminData);
    }
  }


  deleteAdmin(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
