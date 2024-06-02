import type { RestaurantStatus, Uuid } from '@/lib/api-model';
import { putAdminRestaurantsId } from '@/lib/api/admin-restaurants/admin-restaurants';
import { putAdminUsersId } from '@/lib/api/admin-users/admin-users';
import {
  putAdminReportsRestaurantsId,
  putAdminReportsUsersId,
} from '@/lib/api/admin-reports/admin-reports';

export async function closeReportAboutUser(reportId: Uuid, banUser: boolean) {
  return putAdminReportsUsersId(reportId, { ban: banUser });
}

export async function closeReportAboutRestaurant(reportId: Uuid, banRestaurant: boolean) {
  return putAdminReportsRestaurantsId(reportId, { ban: banRestaurant });
}

export async function updateRestaurantStatus(restaurantId: Uuid, status: RestaurantStatus) {
  return putAdminRestaurantsId(restaurantId, { status });
}

export async function updateUserStatus(userId: Uuid, banned: boolean) {
  return putAdminUsersId(userId, { banned });
}
