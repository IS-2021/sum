import { useGetAdminReportsRestaurants } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';
import type { Uuid } from '@/lib/api-model';

/**
 * Gets all reports about given user.
 * @param userId - user id.
 */
export function useReportsAboutUser(userId: Uuid) {
  const query = useGetAdminReportsRestaurants({ userId });
  const { data } = query;

  const reportsAboutUser = computed(() => data.value?.data ?? []);

  return {
    ...query,
    reportsAboutUser,
  };
}
