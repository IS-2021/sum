/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import type { Uuid } from './uuid';
import type { Timestamp } from './timestamp';

export interface BookingDTO {
  id: Uuid;
  meal_id: Uuid;
  picked_up_timestamp?: Timestamp;
  timestamp: Timestamp;
  user_id: Uuid;
}