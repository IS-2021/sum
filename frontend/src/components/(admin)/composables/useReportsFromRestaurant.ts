import { useGetAdminReportsRestaurants } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';

/**
 * Gets all reports from given restaurant.
 * @param restaurantId - restaurant id.
 */
export function useReportsFromRestaurant(restaurantId: string) {
  const query = useGetAdminReportsRestaurants({ restaurantId });
  const { data } = query;

  const reportsFromRestaurant = computed(() => data.value?.data ?? []);

  return {
    ...query,
    reportsFromRestaurant,
  };
}
