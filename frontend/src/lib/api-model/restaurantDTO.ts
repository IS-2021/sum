/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import type { AddressDTO } from './addressDTO';
import type { HoursDTO } from './hoursDTO';
import type { Uuid } from './uuid';
import type { RestaurantStatus } from './restaurantStatus';

export interface RestaurantDTO {
  address: AddressDTO;
  dislikesCount: number;
  hours: HoursDTO;
  id: Uuid;
  imageUrl?: string;
  likesCount: number;
  name: string;
  phoneNumber: string;
  status: RestaurantStatus;
  userId: Uuid;
}
