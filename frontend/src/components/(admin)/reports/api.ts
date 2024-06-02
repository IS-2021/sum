import type { Uuid } from '@/lib/api-model';
import { putAdminRestaurantsId } from '@/lib/api/admin-restaurants/admin-restaurants';

export async function banRestaurant(reportId: Uuid) {
  return putAdminRestaurantsId(reportId, { ban: true });
}

export async function banUser(userId: Uuid) {
  // TBD
}
