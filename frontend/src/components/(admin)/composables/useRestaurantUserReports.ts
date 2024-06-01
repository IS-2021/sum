import { useGetAdminReportsRestaurants } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';

/**
 * Gets all reports about given user.
 * @param userId - user id.
 */
export function useRestaurantUserReports(userId: string) {
  const query = useGetAdminReportsRestaurants();
  const { data } = query;

  const restaurantUserReports = computed(() => {
    if (!data.value?.data) {
      return [];
    }

    return data.value.data.filter((report) => report.userId === userId);
  });

  return {
    ...query,
    restaurantUserReports,
  };
}
