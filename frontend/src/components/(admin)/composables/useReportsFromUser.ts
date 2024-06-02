import { useGetAdminReportsUsers } from '@/lib/api/admin-reports/admin-reports';
import { computed } from 'vue';

/**
 * Gets all reports from given user.
 * @param userId - user id.
 */
export function useReportsFromUser(userId: string) {
  const query = useGetAdminReportsUsers({ userId });
  const { data } = query;

  const reportsFromUser = computed(() => data.value?.data ?? []);

  return {
    ...query,
    reportsFromUser,
  };
}
