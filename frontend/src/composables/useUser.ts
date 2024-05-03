import { computed } from 'vue';
import { useGetUsersMe } from '@/lib/api/users/users';
import { postLogout } from '@/lib/api/auth/auth';

export function useUser() {
  const { isPending, data, refetch } = useGetUsersMe({
    axios: {
      withCredentials: true,
      validateStatus(status) {
        return status < 500;
      },
    },
  });

  const isSignedIn = computed(() => data.value?.status === 200 ?? false);
  const isLoaded = computed(() => !isPending.value);
  const user = computed(() => data.value?.data);

  const signOut = async () => {
    await postLogout({
      withCredentials: true,
    });
    await refetch();
  };

  return {
    isSignedIn,
    isLoaded,
    user,
    signOut,
  };
}

export type UseUser = ReturnType<typeof useUser>;
