import { useGetAdminReportsUsers } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';
import type { Uuid } from '@/lib/api-model';

/**
 * Gets all reports about given restaurant.
 * @param restaurantId - restaurant id.
 */
export function useReportsAboutRestaurant(restaurantId: Uuid) {
  const query = useGetAdminReportsUsers({ restaurantId });
  const { data } = query;

  const reportsAboutRestaurant = computed(() => data.value?.data ?? []);

  return {
    ...query,
    reportsAboutRestaurant,
  };
}
