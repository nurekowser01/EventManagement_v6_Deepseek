export interface Admin {
	id?: number;  // id should be optional

  username: string;
  password: string;
  name: string;
  mobile: string;
  email: string;
  dateOfBirth?: string;  // Optional, depending on your backend
  profileImageUrl?: string;  // Optional field if you have image uploads
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
  lastLoginAt?: string;
  lastLoginIp?: string;
  notes?: string;
}
