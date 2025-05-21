
export enum RoleType {
  SUPER_ADMIN = 'SUPER_ADMIN',
  ADMIN = 'ADMIN',
  MEMBER = 'MEMBER',
  GUEST = 'GUEST',
  // Add any other enum values that exist in your backend RoleType
}

export interface Role {
  id: number;
  type: RoleType | string; // Using RoleType enum but also allow string to be flexible
  details?: string;        // Optional details field
}

export interface Admin {
	id?: number;  // id should be optional

	username: string;
	password: string;
	name: string;
	mobile: string;
	email: string;
	dateOfBirth?: string;  // Optional, depending on your backend
	profileImage?: string;  // Optional field if you have image uploads
	isActive?: boolean;
	createdAt?: string;
	updatedAt?: string;
	lastLoginAt?: string;
	lastLoginIp?: string;
	notes?: string;
	createdBy?: string;
	updatedBy?: string;

	roles?: Role[];
}
