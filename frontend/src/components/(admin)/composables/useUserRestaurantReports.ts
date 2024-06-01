import { useGetAdminReportsUsers } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';

/**
 * Gets all reports about given restaurant.
 * @param restaurantId - restaurant id.
 */
export function useUserRestaurantReports(restaurantId: string) {
  const query = useGetAdminReportsUsers();
  const { data } = query;

  const userRestaurantReports = computed(() => {
    if (!data.value?.data) {
      return [];
    }

    return data.value.data.filter((report) => report.restaurantId === restaurantId);
  });

  return {
    ...query,
    userRestaurantReports,
  };
}
